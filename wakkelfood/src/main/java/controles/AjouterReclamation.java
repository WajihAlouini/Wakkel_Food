package controles;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import gestion_reclamation.entities.reclamation;
import gestion_reclamation.service.reclamationservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class AjouterReclamation {


    @FXML
    private TextArea descriptionfield;

    @FXML
    private ChoiceBox<String> typefield;


    @FXML
    void ajouterReclamation(ActionEvent event) {
        String description = descriptionfield.getText();
        String selectedType = typefield.getValue();

        if (description != null && !description.trim().isEmpty() && selectedType != null) {
            System.out.println("Description: " + description); // Debugging line

            // Use the correct constructor with individual parameters
            reclamation reclamationx = new reclamation(0, 0, null, selectedType, description, null);

            reclamationservice reclamationservicex = new reclamationservice();
            try {

                reclamationservicex.add(reclamationx);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Réclamation ajoutée avec succès");
                alert.show();
            } catch (Exception e) {
                e.printStackTrace(); // print the exception details to console
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de l'ajout de la réclamation.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le champ description ne peut pas être vide.");
            alert.show();
        }
    }






    @FXML
    void initialize() {
        // Initialization code for the controller (if needed)
        typefield.getItems().addAll("Retard", "Articles Manquants", "Commande Incorrecte", "Problème Qualité", "Problème Emballage", "Facturation", "Autre"); // Add appropriate items to the ChoiceBox
    }

}
