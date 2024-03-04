package controles;

import java.sql.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import gestion_reclamation.entities.evaluation;
import gestion_reclamation.service.evaluationservice;

public class ModifierEvaluationController {

    @FXML
    private TextArea commentaireArea;

    @FXML
    private TextField dateField;

    @FXML
    private TextField idCommandeField;

    @FXML
    private TextField id_evaluationField;


    @FXML
    private TextField noteField;

    // Assuming you have a reference to the evaluation service
    private evaluationservice evaluationService;

    public ModifierEvaluationController() {
        // Initialize your evaluation service (you may have it injected or instantiated elsewhere)
        this.evaluationService = new evaluationservice();
    }

    @FXML
    void UpdateEvaluation(ActionEvent event) {
        try {
            System.out.println("UpdateEvaluation method called");

            // Retrieve values from the text fields
            String idEvaluationText = id_evaluationField.getText();
            String idCommandeText = idCommandeField.getText();
            String dateText = dateField.getText(); // You might want to handle date parsing
            String noteText = noteField.getText();
            String commentaire = commentaireArea.getText();

            // Check for empty strings
            if (idCommandeText.isEmpty() || dateText.isEmpty() || noteText.isEmpty()) {
                System.out.println("Required fields are empty. Update aborted.");
                // You might want to show an error message to the user
                return;
            }

            // Add a null check and empty check for idEvaluationText
            if (idEvaluationText != null && !idEvaluationText.isEmpty()) {
                // Parse the values
                int idEvaluation = Integer.parseInt(idEvaluationText); // Assuming idEvaluation is required and cannot be null
                int idCommande = Integer.parseInt(idCommandeText);
                int note = Integer.parseInt(noteText);

                // Create an evaluation object with updated values
                evaluation updatedEvaluation = new evaluation(idEvaluation, idCommande, Date.valueOf(dateText), note, commentaire);

                // Print debug information
                System.out.println("Updating evaluation: " + updatedEvaluation);

                // Perform the update operation
                evaluationService.update(updatedEvaluation);

                // Print success message
                System.out.println("Update successful!");

                // You may want to add additional logic, such as refreshing the UI or displaying a message.
            } else {
                // Handle the case where idEvaluationText is null or empty
                // For example, show an error message to the user
                System.out.println("ID Evaluation is null or empty. Update aborted.");
            }
        } catch (NumberFormatException e) {
            // Handle the exception, for example, show an error message or log it
            e.printStackTrace();
        }

    }

    public void setEvaluation(evaluation selectedEvaluation) {
        if (selectedEvaluation != null) {
            id_evaluationField.setText(String.valueOf(selectedEvaluation.getId_evaluation()));

            // Check if idCommande is not null
            if (selectedEvaluation.getId_commande() != 0) {
                idCommandeField.setText(String.valueOf(selectedEvaluation.getId_commande()));
            } else {
                // Handle the case where idCommande is 0 (default value)
                idCommandeField.setText("DefaultCommandeValue");
            }

            dateField.setText(selectedEvaluation.getDate().toString());
            noteField.setText(String.valueOf(selectedEvaluation.getNote()));
            commentaireArea.setText(selectedEvaluation.getCommentaire());
        }
    }

}
