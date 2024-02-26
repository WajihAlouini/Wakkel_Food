package Services;

import entities.*;
import utils.DataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService implements CrudService<Restaurant> {

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public RestaurantService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Restaurant r) {
        String requete = "INSERT INTO Restaurant (id_category, nom_restaurant, adresse_restaurant) VALUES (?, ?, ?)";

        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, r.getRestaurantCategory().getIdCat());
            pst.setString(2, r.getNom_restaurant());
            pst.setString(3, r.getAdresse_restaurant());
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


    @Override
    public void update(Restaurant restaurant) {
        String updateQuery = "UPDATE restaurant SET id_category = ?, nom_restaurant = ?, adresse_restaurant = ? WHERE id_restaurant = ?";
        try {
            pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, restaurant.getRestaurantCategory().getIdCat());
            pst.setString(2, restaurant.getNom_restaurant());
            pst.setString(3, restaurant.getAdresse_restaurant());
            pst.setInt(4, restaurant.getId_restaurant());

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
    public List<Restaurant> readAll() {
        String query = "SELECT r.id_restaurant, r.nom_restaurant, r.adresse_restaurant, r.restaurant_image, c.category_name " +
                "FROM restaurant r " +
                "INNER JOIN restaurant_category c ON r.id_category = c.id_category";
        List<Restaurant> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                int idRestaurant = rs.getInt("id_restaurant");
                String nomRestaurant = rs.getString("nom_restaurant");
                String adresseRestaurant = rs.getString("adresse_restaurant");
                String imageName = rs.getString("restaurant_image");
                String categoryName = rs.getString("category_name");

                // Log the fetched data for debugging
                System.out.println("ID: " + idRestaurant +
                        ", Name: " + nomRestaurant +
                        ", Address: " + adresseRestaurant +
                        ", Image: " + imageName +
                        ", Category: " + categoryName);

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
            throw new RuntimeException(e);
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
