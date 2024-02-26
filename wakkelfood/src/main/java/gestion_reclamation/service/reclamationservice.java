package gestion_reclamation.service;
import  gestion_reclamation.entities.reclamation;

import java.util.List;


import java.sql.Date;
import java.time.LocalDate;

import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class reclamationservice implements IService<reclamation>{
    private Connection conn;
    private PreparedStatement pst;

    public reclamationservice() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(reclamation r) {
        // Fetch the latest id_commande from the commande table
        int latestIdCommande = getLatestIdCommande();

        // Use the latestIdCommande in the insert query
        String query = "INSERT INTO Reclamation (id_reclamation, id_commande, date, type, description, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, r.getId_reclamation());
            pst.setInt(2, latestIdCommande); // Use the latestIdCommande here
            pst.setDate(3, Date.valueOf(LocalDate.now()));
            pst.setString(4, r.getType());
            pst.setString(5, r.getDecription());

            // Check if 'statut' is provided, if not, set it to 'Non resolu'
            if (r.getStatut() != null) {
                pst.setString(6, r.getStatut());
            } else {
                pst.setString(6, "Non resolu");
            }

            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    // Add this method to fetch the latest id_commande
    private int getLatestIdCommande() {
        String selectLatestIdCommandeQuery = "SELECT id_commande FROM commande ORDER BY id_commande DESC LIMIT 1";

        try (PreparedStatement selectStatement = conn.prepareStatement(selectLatestIdCommandeQuery);
             ResultSet resultSet = selectStatement.executeQuery()) {

            if (resultSet.next()) {
                // Retrieve the latest id_commande
                return resultSet.getInt("id_commande");
            } else {
                // Handle the case where no records are found in the commande table
                System.out.println("No records found in commande table");
                return -1; // or another appropriate default value
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            return -1; // or another appropriate default value
        }
    }





    @Override
    public void delete(int id) {
        String query = "DELETE FROM reclamation WHERE id_reclamation = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(reclamation reclamation) {
        String query = "UPDATE reclamation SET id_commande = ?, date = ?, type = ?, description = ?, statut = ? WHERE id_reclamation = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, reclamation.getId_commande());
            pst.setDate(2, reclamation.getDate());
            pst.setString(3, reclamation.getType());
            pst.setString(4, reclamation.getDecription());
            pst.setString(5, reclamation.getStatut());
            pst.setInt(6, reclamation.getId_reclamation());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public List<reclamation> readAll() {
        List<reclamation> list = new ArrayList<>();
        try {
            pst = conn.prepareStatement("SELECT * FROM reclamation");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reclamation rec = new reclamation(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                );
                list.add(rec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Number of reclamations retrieved: " + list.size()); // Add this line
        return list;
    }


    @Override
    public reclamation readById(int id) {
        return null;
    }
}