package service;
import entities.evaluation;
import entities.reclamation;
import utils.DataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class evaluationservice implements service.IServiceReclamation<evaluation> {
    private Connection conn;
    private PreparedStatement pst;

    public evaluationservice() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(reclamation r, InputStream imageInputStream) {

    }

    @Override
    public void add(evaluation evaluation) {

        // Check for null values
        if (evaluation.getDate() == null) {
            System.out.println("Date cannot be null. Insertion aborted.");
            return; // or throw a specific exception
        }

        String query = "INSERT INTO evaluation (id_evaluation, email_e, date, note, commentaire) VALUES (?, ?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, evaluation.getId_evaluation());
            pst.setString(2, evaluation.getEmail_e()); // Use the latestIdCommande here
            pst.setDate(3, evaluation.getDate());
            pst.setInt(4, evaluation.getNote());
            pst.setString(5, evaluation.getCommentaire());

            pst.executeUpdate();
        } catch (SQLException ex) {
            // Log the exception or throw a specific exception
            System.out.println("Failed to add evaluation. " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM evaluation WHERE id_evaluation = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            // Log the exception or throw a specific exception
            System.out.println("Failed to delete evaluation. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(evaluation evaluation) {
        String query = "UPDATE evaluation SET note = ?, commentaire = ? WHERE id_evaluation = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, evaluation.getNote());
            pst.setString(2, evaluation.getCommentaire());
            pst.setInt(3, evaluation.getId_evaluation());
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Update successful for evaluation with ID: " + evaluation.getId_evaluation());
            } else {
                System.out.println("Update did not affect any rows for evaluation with ID: " + evaluation.getId_evaluation());
            }

        } catch (SQLException e) {
            // Log the exception or throw a specific exception
            System.out.println("Failed to update evaluation. " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<evaluation> readAll() {
        String query = "SELECT * FROM evaluation";
        List<evaluation> list = new ArrayList<>();
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new evaluation(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getInt(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            // Log the exception or throw a specific exception
            System.out.println("Failed to retrieve evaluations. " + e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public evaluation readById(int id) {
        String query = "SELECT * FROM evaluation WHERE id_evaluation = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new evaluation(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getInt(4),
                        rs.getString(5)
                );
            }
        } catch (SQLException e) {
            // Log the exception or throw a specific exception
            System.out.println("Failed to retrieve evaluation by ID. " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
    public double calculateMoyenne() {
        List<evaluation> evaluations = readAll();

        if (evaluations.isEmpty()) {
            return 0.0; // Handle the case where there are no evaluations
        }

        double total = 0;
        for (evaluation eval : evaluations) {
            total += eval.getNote();
        }

        return total / evaluations.size();
    }
}
