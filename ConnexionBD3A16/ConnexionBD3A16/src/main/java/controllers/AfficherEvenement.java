package controllers;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.DataSource;

public class AfficherEvenement {
    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private ListView<Evenement> listView;
    @FXML
    private TextField idSupp;
    @FXML
    private Label labelMenu;
    @FXML
    private Button update;
    @FXML
    void Menu(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoard.fxml"));
        try {
            Parent root = loader.load();
            DashBoard controller = loader.getController();
            labelMenu.getScene().setRoot(root);
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
            listView.setItems(items);
        } catch ( SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnClick(MouseEvent mouseEvent) {
        int selectedItem = listView.getSelectionModel().getSelectedItem().getIdEvent();
        idSupp.setText(String.valueOf(selectedItem));
    }
    public void delete(ActionEvent actionEvent) throws SQLException {
        int selectedItem = listView.getSelectionModel().getSelectedItem().getIdEvent();
        idSupp.setText(String.valueOf(selectedItem));
        String sql = "DELETE FROM evenement WHERE idEvent = ?";
        try {
            // Create a confirmation dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Confirmation");
            dialog.setContentText("Are you sure you want to delete this product?");
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
        try {
            Parent root = loader.load();
            ModifierEvenement controller = loader.getController();
            update.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}