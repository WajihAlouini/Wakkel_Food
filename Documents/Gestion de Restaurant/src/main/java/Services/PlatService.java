package Services;

import entities.Plat;
import entities.Restaurant;
import utils.DataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatService implements CrudService<Plat> {

    private Connection conn;
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
}
