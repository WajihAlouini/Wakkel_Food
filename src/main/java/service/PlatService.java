package service;

import entities.Plat;
import entities.Restaurant;
import org.w3c.dom.Element;
import utils.DataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class PlatService implements Services.CrudService<Plat> {

    private static Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public PlatService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Plat plat) {
        String requete = "INSERT INTO Plat (id_restaurant, nom_plat, prix, ingredient, plat_image) VALUES (?, ?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, plat.getRestaurant().getId_restaurant());
            pst.setString(2, plat.getNom_plat());
            pst.setDouble(3, plat.getPrix());
            pst.setString(4, plat.getIngredient());
            pst.setString(5, plat.getPimgSrc()); // Add the image source
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Plat plat) {
        String checkExistenceQuery = "SELECT * FROM Plat WHERE id_plat = ?";
        String updateQuery = "UPDATE Plat SET id_restaurant=?, nom_plat=?, prix=?, ingredient=?, plat_image=? WHERE id_plat=?";

        try {
            // Check if the Plat with the given ID exists
            pst = conn.prepareStatement(checkExistenceQuery);
            pst.setInt(1, plat.getId_plat());
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                System.out.println("Plat with ID " + plat.getId_plat() + " does not exist.");
                return;
            }

            // Perform the update
            pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, plat.getRestaurant().getId_restaurant());
            pst.setString(2, plat.getNom_plat());
            pst.setDouble(3, plat.getPrix());
            pst.setString(4, plat.getIngredient());
            pst.setString(5, plat.getPimgSrc()); // Update the image source
            pst.setInt(6, plat.getId_plat());
            pst.executeUpdate();

            System.out.println("Plat with ID " + plat.getId_plat() + " modified successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void delete(int idPlat) {
        String deleteQuery = "DELETE FROM plat WHERE id_plat = ?";
        try {
            // Check if the ID exists first
            String selectQuery = "SELECT * FROM plat WHERE id_plat = ?";
            pst = conn.prepareStatement(selectQuery);
            pst.setInt(1, idPlat);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                System.out.println("ID inexistant.");
                return;
            }

            // If the ID exists, proceed with deletion
            pst = conn.prepareStatement(deleteQuery);
            pst.setInt(1, idPlat);
            pst.executeUpdate();
            System.out.println("Plat avec l'ID " + idPlat + " supprimé avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






    @Override
    public List<Plat> readAll() {
        String query = "SELECT * FROM plat";
        List<Plat> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                Plat plat = new Plat();
                plat.setId_plat(rs.getInt("id_plat"));
                plat.setNomPlat(rs.getString("nom_plat"));
                plat.setPrix(rs.getDouble("prix"));
                plat.setIngredient(rs.getString("ingredient"));

                // Create a Restaurant object and set its ID
                Restaurant restaurant = new Restaurant();
                restaurant.setId_restaurant(rs.getInt("id_restaurant"));

                // Set the Restaurant object in the Plat
                plat.setRestaurant(restaurant);

                // Add the Plat to the list
                list.add(plat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Plat> getPlatsByRestaurantId(int restaurantId) {
        String query = "SELECT * FROM plat WHERE id_restaurant = ?";
        List<Plat> list = new ArrayList<>();

        try {
            // Use a PreparedStatement to safely set the parameter
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, restaurantId);

                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    Plat plat = new Plat();
                    plat.setId_plat(rs.getInt("id_plat"));
                    plat.setNomPlat(rs.getString("nom_plat"));
                    plat.setPrix(rs.getDouble("prix"));
                    plat.setIngredient(rs.getString("ingredient"));
                    plat.setPimgSrc((rs.getString("plat_image")));

                    // Create a Restaurant object and set its ID
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId_restaurant(rs.getInt("id_restaurant"));

                    // Set the Restaurant object in the Plat
                    plat.setRestaurant(restaurant);

                    // Add the Plat to the list
                    list.add(plat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    public List<Plat> getPlatsForRestaurant(Restaurant restaurant) {
        String query = "SELECT * FROM plat WHERE id_restaurant = ?";
        List<Plat> list = new ArrayList<>();

        try {
            // Use a PreparedStatement to safely set the parameter
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, restaurant.getId_restaurant());

                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    Plat plat = new Plat();
                    plat.setId_plat(rs.getInt("id_plat"));
                    plat.setNomPlat(rs.getString("nom_plat"));
                    plat.setPrix(rs.getDouble("prix"));
                    plat.setIngredient(rs.getString("ingredient"));
                    plat.setPimgSrc(rs.getString("plat_image"));


                    // Set the Restaurant object in the Plat
                    plat.setRestaurant(restaurant);

                    // Add the Plat to the list
                    list.add(plat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }



    @Override
    public Plat readById(int id) {
        // Implement the method to read a specific Plat by ID
        return null;
    }

    public List<Plat> getSearchResults(String keyword) {
        List<Plat> allPlats = readAll();
        List<Plat> searchResults = new ArrayList<>();

        // Specify the attributes to search across
        String[] searchableAttributes = {"nom_plat", "prix", "ingredient"}; // Add more attributes as needed

        for (Plat plat : allPlats) {
            // Check if any of the specified attributes contain the keyword
            if (containsKeyword(plat, searchableAttributes, keyword)) {
                searchResults.add(plat);
            }
        }

        return searchResults;
    }

    private boolean containsKeyword(Plat plat, String[] attributes, String keyword) {
        // Iterate through specified attributes and check if any contains the keyword
        for (String attribute : attributes) {
            String attributeValue = getAttributeValue(plat, attribute);
            if (attributeValue != null && attributeValue.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String getAttributeValue(Plat plat, String attribute) {
        // Use reflection to dynamically get the attribute value from the Plat object
        try {
            String methodName = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
            java.lang.reflect.Method method = Plat.class.getMethod(methodName);

            // Check the type of the attribute
            Class<?> returnType = method.getReturnType();
            if (returnType.equals(String.class)) {
                return (String) method.invoke(plat);
            } else if (returnType.equals(double.class)) {
                // Handle the case where the attribute is of type double
                return String.valueOf((double) method.invoke(plat));
            } else {
                // Handle other types as needed
                System.err.println("Error: Unsupported attribute type for '" + attribute + "'.");
                return null;
            }
        } catch (NoSuchMethodException e) {
            // Handle the case where the getter method does not exist
            System.err.println("Error: Getter method for attribute '" + attribute + "' not found.");
            return null;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return null;
        }
    }


    public static List<Plat> searchPlats(String keyword) {
        String query = "SELECT p.id_plat, p.nom_plat, p.prix, p.ingredient, p.plat_image " +
                "FROM plat p " +
                "WHERE ";

        // Add conditions for each column related to plat in the plat table
        String[] platSearchColumns = {"p.nom_plat", "p.prix", "p.ingredient"}; // Add more columns as needed

        for (int i = 0; i < platSearchColumns.length; i++) {
            query += platSearchColumns[i] + " LIKE ?";
            if (i < platSearchColumns.length - 1) {
                query += " OR ";
            }
        }

        List<Plat> list = new ArrayList<>();

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            for (int i = 1; i <= platSearchColumns.length; i++) {
                pst.setString(i, "%" + keyword + "%");
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idPlat = rs.getInt("id_plat");
                    String nomPlat = rs.getString("nom_plat");
                    double prix = rs.getDouble("prix");
                    String ingredient = rs.getString("ingredient");
                    String imageName = rs.getString("plat_image");

                    // Create the Plat object with the fetched data
                    // Assuming there's no PlatCategory in your data model
                    list.add(new Plat(nomPlat,new Restaurant(),prix, ingredient, imageName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching plats by name in the database", e);
        }

        return list;
    }


}
