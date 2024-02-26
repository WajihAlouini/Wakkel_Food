package Services;

import entities.RestaurantCategory;
import Services.CrudService;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RestaurantCategoryService implements CrudService<RestaurantCategory> {

    private Connection conn;
    private PreparedStatement pst;
    private Statement ste;

    public RestaurantCategoryService() {
        conn = DataSource.getInstance().getCnx();

    }

    @Override
    public void add(RestaurantCategory category) {
        String query = "INSERT INTO restaurant_category (category_name) VALUES (?)";

        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, category.getCategoryName());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        String deleteQuery = "DELETE FROM restaurant_category WHERE id_category = ?";
        try {
            // Check if the ID exists first
            String selectQuery = "SELECT * FROM restaurant_category WHERE id_category = ?";
            pst = conn.prepareStatement(selectQuery);
            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                System.out.println("ID inexistant.");
                return;
            }

            // If the ID exists, proceed with deletion
            pst = conn.prepareStatement(deleteQuery);
            pst.setInt(1, categoryId);
            pst.executeUpdate();
            System.out.println("Catégorie avec l'ID " + categoryId + " supprimée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(RestaurantCategory rc) {
        String updateQuery = "UPDATE restaurant_category SET category_name = ? WHERE id_category = ?";
        try {
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, rc.getCategoryName());
            pst.setInt(2, rc.getIdCat());
            pst.executeUpdate();
            System.out.println("Catégorie avec l'ID " + rc.getIdCat() + " mise à jour avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestaurantCategory> readAll() {
        String query = "SELECT * FROM restaurant_category";
        List<RestaurantCategory> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                list.add(new RestaurantCategory(rs.getInt("id_category"), rs.getString("category_name"), rs.getString("category_image")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public RestaurantCategory readById(int id) {
        // Implement the method to read a specific RestaurantCategory by ID
        return null;
    }
    public RestaurantCategory getCategoryByName(String categoryName) {
        String query = "SELECT * FROM restaurant_category WHERE category_name = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, categoryName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new RestaurantCategory(rs.getInt("id_category"), rs.getString("category_name"),rs.getString("category_image"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
