package com.example.test_javafx2;

import com.example.test_javafx2.back;
import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.EvenementService;
import utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

public class ModifierEvenement {

    @FXML
    private DatePicker DateD;

    @FXML
    private DatePicker DateF;

    @FXML
    private TextField EventN;

    @FXML
    private Label labelMenu;
    @FXML
    private TextField IdEvent;
    @FXML
    private Button update;

    @FXML
    void Menu(MouseEvent event) {
        Connection cnx = DataSource.getInstance().getCnx();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuBack.fxml"));
        try {
            Parent root = loader.load();
            MenuBackController controller = loader.getController();
            labelMenu.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }





    @FXML
    void UpdateE(ActionEvent event) {
        try {
            // Get values from text fields
            int idE = Integer.parseInt(IdEvent.getText());
            String eventName = EventN.getText();
            LocalDate startDate = DateD.getValue();
            LocalDate endDate = DateF.getValue();

            // Create an Evenement object with the retrieved values
            Evenement evenement = new Evenement(idE,eventName, startDate, endDate);

             // Set the correct ID of the record you want to update

            // Call the update method from your service class (assuming you have it)
            EvenementService evenementService = new EvenementService();
            evenementService.modifier(evenement);

            // Display an alert based on the update result
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Evenement updated");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Handle the exception if the conversion fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid numeric values.");
            alert.showAndWait();
        }
    }

}