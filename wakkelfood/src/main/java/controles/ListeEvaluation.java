package controles;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import gestion_reclamation.entities.evaluation;
import gestion_reclamation.service.evaluationservice;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.ResourceBundle;

public class ListeEvaluation {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<evaluation> evaluationsTable;

    @FXML
    private TableColumn<evaluation, Integer> idEvaluationColumn;

    @FXML
    private TableColumn<evaluation, Integer> idCommandeColumn;

    @FXML
    private TableColumn<evaluation, Date> dateColumn;

    @FXML
    private TableColumn<evaluation, Integer> noteColumn;

    @FXML
    private TableColumn<evaluation, String> commentaireColumn;

    @FXML
    void initialize() {
        assert evaluationsTable != null : "fx:id=\"evaluationsTable\" was not injected: check your FXML file 'ListeEvaluation.fxml'.";

        // Initialize columns (you can set cell value factories if needed)

        // Populate the TableView
        populateEvaluationsTable();
    }

    private void populateEvaluationsTable() {
        evaluationservice evaluationservicex = new evaluationservice();
        evaluationsTable.getItems().setAll(evaluationservicex.readAll());
    }

    // Method to handle the delete action
    @FXML
    private void DeleteEvaluation() {
        evaluationservice evaluationservicex = new evaluationservice();
        evaluation selectedEvaluation = evaluationsTable.getSelectionModel().getSelectedItem();

        if (selectedEvaluation != null) {
            evaluationservicex.delete(selectedEvaluation.getId_evaluation());
            populateEvaluationsTable(); // Refresh the TableView after deletion
        }
    }

    // Method to handle the update action
    @FXML
    private void UpdateEvaluation() {
        evaluation selectedEvaluation = evaluationsTable.getSelectionModel().getSelectedItem();

        if (selectedEvaluation != null) {
            try {
                // Load the UpdateEvaluation.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvaluation.fxml"));
                AnchorPane root = loader.load();

                // Pass the selected evaluation to the controller of the new window
                ModifierEvaluationController modifierController = loader.getController();
                modifierController.setEvaluation(selectedEvaluation);

                // Create a new Stage for the UpdateEvaluation window
                Stage updateStage = new Stage();
                updateStage.initModality(Modality.WINDOW_MODAL);
                updateStage.initOwner(evaluationsTable.getScene().getWindow());
                updateStage.setScene(new Scene(root));

                // Show the UpdateEvaluation window
                updateStage.showAndWait();

                // Refresh the TableView after the update (if needed)
                populateEvaluationsTable();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }
    }



}
