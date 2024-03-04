package com.example.test_javafx2;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

import entities.Evenement;
import entities.Participation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.DataSource;

public class back {

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private ListView<Evenement> listview;
    @FXML
    private TextField idSupp;

    @FXML
    private Button update;
    @FXML
    private Label labelAdd;
    @FXML
    private ListView<Participation> ListV;
    @FXML
    private Button updatee;
    public back() {
    }
    @FXML
    void AjouterEvent(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterEvenement.fxml"));
        try {
            Parent root = loader.load();
            AjouterEvenement controller = loader.getController();
            labelAdd.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Créer une liste d'objets Evenement
        ObservableList<Evenement> items = FXCollections.observableArrayList();
        try {

            String req = "SELECT * FROM evenement";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvent(rs.getInt(1));
                evenement.setEventName(rs.getString(2));
                evenement.setDateDebut(Date.valueOf(rs.getDate("dateDebut").toLocalDate())); // Assuming dateDebut is a Date type in your database
                evenement.setDateFin(Date.valueOf(rs.getDate("dateFin").toLocalDate())); // Assuming dateFin is a Date type in your database

                items.add(evenement);
            }

            // Lier la liste à la ListView
            listview.setItems(items);
        } catch ( SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Participation> participationItems = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM participation";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Participation participation = new Participation();
                participation.setIdP(rs.getInt(1));
                participation.setIdUser(rs.getInt(2));
                participation.setIdEvent(rs.getInt(3));
                participation.setNbrPlace(rs.getInt(4));
                participation.setEmailUser(rs.getString(5));

                participationItems.add(participation);  // Fix here: Use participationItems instead of items
            }

            // Assuming your ListView for Participation is named 'ListV'
            ListV.setItems(participationItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

        public void Onclick(MouseEvent mouseEvent) {
        int selectedItem = listview.getSelectionModel().getSelectedItem().getIdEvent();
        idSupp.setText(String.valueOf(selectedItem));
    }

    public void delete(ActionEvent actionEvent) throws SQLException{
        int selectedItem = listview.getSelectionModel().getSelectedItem().getIdEvent();
        idSupp.setText(String.valueOf(selectedItem));
        String sql = "DELETE FROM evenement WHERE idEvent = ?";
        try {
            // Create a confirmation dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Confirmation");
            dialog.setContentText("Are you sure you want to delete this p?");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            // Show the dialog and wait for the admin's response
            Optional<ButtonType> result = dialog.showAndWait();

            // If the admin chooses "Yes", delete the product
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                    int idSuppValue = Integer.parseInt(idSupp.getText());
                    preparedStatement.setInt(1, idSuppValue);
                    preparedStatement.executeUpdate();
                    System.out.println("Event Deleted successfully!");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Event Deleted successfully!");
                    alert.showAndWait();
                    initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(MouseEvent mouseEvent) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifierEvenement.fxml"));
            try {
                Parent root = loader.load();
                ModifierEvenement controller = loader.getController();
                update.getScene().setRoot(root);
            }catch (IOException e){
                e.printStackTrace();
            }

    }
    public void updatee(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifierParticipation.fxml"));
        try {
            Parent root = loader.load();
            ModifierParticipation controller = loader.getController();
            updatee.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}