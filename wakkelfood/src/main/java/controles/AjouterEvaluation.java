package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import gestion_reclamation.entities.evaluation;
import gestion_reclamation.service.evaluationservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Date; // Import java.sql.Date
import java.sql.SQLException;

public class    AjouterEvaluation {

    @FXML
    private TextArea commentaireField;

    @FXML
    private ChoiceBox<Integer> notefield;

    @FXML
    void ajouterEvaluation(ActionEvent event) {
        String commentaire = commentaireField.getText();
        Integer selectedNote = notefield.getValue();

        if (commentaire != null && !commentaire.trim().isEmpty() && selectedNote != null) {
            System.out.println("Commentaire: " + commentaire); // Debugging line

            // Set the 'date' field to the current date
            Date currentDate = new Date(System.currentTimeMillis()); // Convert to java.sql.Date

            // Use the correct constructor with individual parameters
            evaluation evaluationx = new evaluation(0, 0, currentDate, selectedNote, commentaire);

            evaluationservice evaluationservicex = new evaluationservice();

            try {
                evaluationservicex.add(evaluationx);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Evaluation ajoutée avec succès");
                alert.show();
            } catch (Exception e) {
                e.printStackTrace(); // print the exception details to console
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de l'ajout de l'évaluation.");
                alert.show();
            }
        } else {
            // Display an error message for an empty comment field
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le champ commentaire ne peut pas être vide.");
            alert.show();
        }
    }


    @FXML
    void initialize() {
        // Set up the ChoiceBox with numbers from 1 to 5
        ObservableList<Integer> noteOptions = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        notefield.setItems(noteOptions);
        notefield.setValue(1); // Set default value if needed
    }
}
