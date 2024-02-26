package controllers;

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

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class AfficherParticipation {
    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private ListView<Participation> ListV;

    @FXML
    private TextField idSupp;

    @FXML
    private Label labelB;

    @FXML
    private Button update;


    public void initialize() {
        // Créer une liste d'objets Evenement
        ObservableList<Participation> items = FXCollections.observableArrayList();
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


                items.add(participation);
            }

            // Lier la liste à la ListView
            ListV.setItems(items);
        } catch ( SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void delete(ActionEvent event)throws SQLException {
        int selectedItem = ListV.getSelectionModel().getSelectedItem().getIdP();
        idSupp.setText(String.valueOf(selectedItem));
        String sql = "DELETE FROM participation WHERE idP = ?";
        try {
            // Create a confirmation dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Confirmation");
            dialog.setContentText("Are you sure you want to delete this participation?");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            // Show the dialog and wait for the admin's response
            Optional<ButtonType> result = dialog.showAndWait();

            // If the admin chooses "Yes", delete the product
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                    int idSuppValue = Integer.parseInt(idSupp.getText());
                    preparedStatement.setInt(1, idSuppValue);
                    preparedStatement.executeUpdate();
                    System.out.println("Participation Deleted successfully!");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Participation Deleted successfully!");
                    alert.showAndWait();
                    initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } {

    }
    @FXML
    void menu(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterParticipation.fxml"));
        try {
            Parent root = loader.load();
            AjouterParticipation controller = loader.getController();
            labelB.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML

        public void update(MouseEvent mouseEvent) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierParticipation.fxml"));
            try {
                Parent root = loader.load();
                ModifierParticipation controller = loader.getController();
                update.getScene().setRoot(root);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    public void OnClick(MouseEvent mouseEvent) {
        int selectedItem = ListV.getSelectionModel().getSelectedItem().getIdP();
        idSupp.setText(String.valueOf(selectedItem));
    }
}


