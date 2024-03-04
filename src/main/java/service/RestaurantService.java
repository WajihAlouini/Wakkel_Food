package Services;

import entities.*;
import utils.DataSource;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService implements CrudService<Restaurant> {

    private static Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public RestaurantService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Restaurant r) {
        String requete = "INSERT INTO Restaurant (id_category, nom_restaurant, adresse_restaurant, restaurant_image) VALUES (?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, r.getRestaurantCategory().getIdCat());
            pst.setString(2, r.getNom_restaurant());
            pst.setString(3, r.getAdresse_restaurant());
            pst.setString(4, r.getImgSrc());  // Corrected index to 4 for restaurant_image
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id_restaurant) {
        String selectQuery = "SELECT * FROM restaurant WHERE id_restaurant = ?";
        String deleteQuery = "DELETE FROM restaurant WHERE id_restaurant = ?";

        try {
            // Check if the ID exists first
            pst = conn.prepareStatement(selectQuery);
            pst.setInt(1, id_restaurant);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                System.out.println("Restaurant avec l'ID " + id_restaurant + " n'existe pas. Suppression annulée.");
                return;
            }

            // Proceed with the deletion
            pst = conn.prepareStatement(deleteQuery);
            pst.setInt(1, id_restaurant);
            pst.executeUpdate();
            System.out.println("Restaurant avec l'ID " + id_restaurant + " supprimé avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Restaurant> getSearchResults(String keyword) {
        // Assuming you have a method or logic to fetch all restaurants
        List<Restaurant> allRestaurants = readAll();
        List<Restaurant> searchResults = new ArrayList<>();

        // Specify the attributes to search across
        String[] searchableAttributes = {"nom_restaurant", "adresse_restaurant"}; // Add more attributes as needed

        for (Restaurant restaurant : allRestaurants) {
            // Check if any of the specified attributes contain the keyword
            if (containsKeyword(restaurant, searchableAttributes, keyword)) {
                searchResults.add(restaurant);
            }
        }

        return searchResults;
    }

    private boolean containsKeyword(Restaurant restaurant, String[] attributes, String keyword) {
        // Iterate through specified attributes and check if any contains the keyword
        for (String attribute : attributes) {
            String attributeValue = getAttributeValue(restaurant, attribute);
            if (attributeValue != null && attributeValue.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String getAttributeValue(Restaurant restaurant, String attribute) {
        // Use reflection to dynamically get the attribute value from the Restaurant object
        try {
            String methodName = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
            return (String) Restaurant.class.getMethod(methodName).invoke(restaurant);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Restaurant> searchRestaurants(String keyword) {
        String query = "SELECT r.id_restaurant, r.nom_restaurant, r.adresse_restaurant, r.restaurant_image, c.category_name " +
                "FROM restaurant r " +
                "INNER JOIN restaurant_category c ON r.id_category = c.id_category " +
                "WHERE ";

        // Add conditions for each column in the restaurant table
        String[] allColumns = {"r.nom_restaurant", "r.adresse_restaurant", "r.restaurant_image", "c.category_name", /* add more columns as needed */};

        for (int i = 0; i < allColumns.length; i++) {
            query += allColumns[i] + " LIKE ?";
            if (i < allColumns.length - 1) {
                query += " OR ";
            }
        }

        List<Restaurant> list = new ArrayList<>();

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            for (int i = 1; i <= allColumns.length; i++) {
                pst.setString(i, "%" + keyword + "%");
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idRestaurant = rs.getInt("id_restaurant");
                    String nomRestaurant = rs.getString("nom_restaurant");
                    String adresseRestaurant = rs.getString("adresse_restaurant");
                    String imageName = rs.getString("restaurant_image");
                    String categoryName = rs.getString("category_name");

                    // Create the Restaurant object with the fetched data
                    RestaurantCategory restaurantCategory = new RestaurantCategory();
                    restaurantCategory.setCategoryName(categoryName);

                    // Check for null image name
                    String imgSrc = (imageName != null) ? imageName : "default-image.jpg";

                    // Pass the image name to the Restaurant constructor
                    list.add(new Restaurant(idRestaurant, restaurantCategory, nomRestaurant, adresseRestaurant, imgSrc));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching restaurants in the database", e);
        }

        return list;
    }



    @Override
    public void update(Restaurant restaurant) {
        String updateQuery = "UPDATE restaurant SET id_category = ?, nom_restaurant = ?, adresse_restaurant = ?, restaurant_image = ? WHERE id_restaurant = ?";
        try {
            pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, restaurant.getRestaurantCategory().getIdCat());
            pst.setString(2, restaurant.getNom_restaurant());
            pst.setString(3, restaurant.getAdresse_restaurant());
            pst.setString(4, restaurant.getImgSrc());

            // Check if a new image path is provided


            pst.setInt(5, restaurant.getId_restaurant());  // Set the ID for the WHERE clause

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Restaurant avec l'ID " + restaurant.getId_restaurant() + " mis à jour avec succès.");
            } else {
                System.out.println("Aucun restaurant avec l'ID " + restaurant.getId_restaurant() + " n'a été trouvé.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public  List<Restaurant> readAll() {
        String query = "SELECT r.id_restaurant, r.nom_restaurant, r.adresse_restaurant, r.restaurant_image, c.category_name " +
                "FROM restaurant r " +
                "INNER JOIN restaurant_category c ON r.id_category = c.id_category";
        List<Restaurant> list = new ArrayList<>();
        try (Statement ste = conn.createStatement(); ResultSet rs = ste.executeQuery(query)) {
            while (rs.next()) {
                int idRestaurant = rs.getInt("id_restaurant");
                String nomRestaurant = rs.getString("nom_restaurant");
                String adresseRestaurant = rs.getString("adresse_restaurant");
                String imageName = rs.getString("restaurant_image");
                String categoryName = rs.getString("category_name");



                // Create the Restaurant object with the fetched data
                RestaurantCategory restaurantCategory = new RestaurantCategory();
                restaurantCategory.setCategoryName(categoryName);

                // Check for null image name
                String imgSrc = (imageName != null) ? imageName : "default-image.jpg"; // Provide a default image name or handle as needed

                // Pass the image name to the Restaurant constructor
                list.add(new Restaurant(idRestaurant, restaurantCategory, nomRestaurant, adresseRestaurant, imgSrc));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            throw new RuntimeException("Error fetching restaurants from the database", e);
        }
        return list;
    }





    @Override
    public Restaurant readById(int id) {
        // Implement the method to read a specific Restaurant by ID
        // ...
        return null;
    }
}
