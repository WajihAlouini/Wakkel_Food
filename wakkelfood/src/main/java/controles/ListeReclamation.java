package controles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import gestion_reclamation.entities.reclamation;
import gestion_reclamation.service.reclamationservice;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ListeReclamation {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<reclamation> reclamationsTable;

    @FXML
    private TableColumn<reclamation, Integer> idReclamationColumn;

    // Other TableColumn declarations...

    @FXML
    private TableColumn<reclamation, String> statutColumn;

    @FXML
    private Button supprimerButton;

    // Add ComboBox for selecting statut
    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    void initialize() {
        assert reclamationsTable != null : "fx:id=\"reclamationsTable\" was not injected: check your FXML file 'ListeReclamation.fxml'.";

        // Initialize columns (you can set cell value factories if needed)

        // Initialize statutComboBox with enum values
        statutComboBox.getItems().addAll("Resolu", "En cours de resolution", "Non resolu");

        // Populate the TableView
        populateReclamationsTable();
    }

    private void populateReclamationsTable() {
        reclamationservice reclamationservicex = new reclamationservice();
        reclamationsTable.getItems().setAll(reclamationservicex.readAll());
    }

    @FXML
    void DeleteReclamation() {
        reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            reclamationservice reclamationservicex = new reclamationservice();
            reclamationservicex.delete(selectedReclamation.getId_reclamation());
            populateReclamationsTable();
        } else {
            System.out.println("No reclamation selected for deletion.");
        }
    }

    @FXML
    void UpdateReclamation() {
        reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            // Get updated values from UI components
            String updatedStatut = statutComboBox.getValue();  // Assuming you have a ComboBox for statut

            // Update all columns of the selected reclamation
            selectedReclamation.setStatut(updatedStatut);
            // Update other columns similarly

            // Call the update method from your service
            reclamationservice reclamationservicex = new reclamationservice();
            reclamationservicex.update(selectedReclamation);

            // Refresh the TableView
            populateReclamationsTable();
        } else {
            System.out.println("No reclamation selected for update.");
        }
    }
}
