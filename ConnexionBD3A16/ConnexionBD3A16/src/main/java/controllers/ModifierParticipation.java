package controllers;



import entities.Participation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import service.ParticipationService;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.DataSource;

import java.io.IOException;
import java.sql.Connection;


public class ModifierParticipation {


    @FXML
    private TextField emailU;

    @FXML
    private Label labelM;
    @FXML
    private TextField idevent;
    @FXML
    private TextField nbrp;
    @FXML
    private TextField idp;
    @FXML
    private TextField idU;
    @FXML
    private Button updateP;

    @FXML
    void Menu(MouseEvent event) {

    Connection cnx = DataSource.getInstance().getCnx();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
        try {
        Parent root = loader.load();
        back controller = loader.getController();
        labelM.getScene().setRoot(root);
    }
        catch (IOException e)
    {
        e.printStackTrace();
    }
    }
    @FXML

    void updateP(ActionEvent event) {
        try {
            // Get values from text fields
            int idPa = Integer.parseInt(idp.getText());
            int idUs = Integer.parseInt(idU.getText());
            int ide = Integer.parseInt(idevent.getText());
            int nbrPlace = Integer.parseInt(nbrp.getText());
            String emailUs = emailU.getText(); // Replace with the actual ID of your email TextField

            // Create a Participation object with the retrieved values
            Participation participation = new Participation(idPa, idUs, ide, nbrPlace, emailUs);

            // Set the correct ID of the record you want to update

            // Call the update method from your service class (assuming you have it)
            ParticipationService participationService = new ParticipationService();
            participationService.Modifier(participation);

            // Display an alert based on the update result
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Participation updated");
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