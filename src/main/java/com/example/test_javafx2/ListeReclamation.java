package com.example.test_javafx2;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.reclamation;
import service.reclamationservice;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

    @FXML
    void ExportPdf() {
        ObservableList<reclamation> data = reclamationsTable.getItems();

        // Get the application's current working directory
        String currentWorkingDirectory = System.getProperty("user.dir");

        // Set the base file name
        String baseFileName = "Reclamations_export";

        // Initialize the file number
        int fileNumber = 1;

        // Check for existing files
        File file;
        do {
            file = new File(currentWorkingDirectory, baseFileName + "_" + fileNumber + ".pdf");
            fileNumber++;
        } while (file.exists());

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Choose a different font and adjust the font size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

                // Write the document title in black
                contentStream.setNonStrokingColor(0, 0, 0); // Black color for the title
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Liste des réclamations pour le " + java.time.LocalDate.now());
                contentStream.newLineAtOffset(0, -30);

                // Reset color to red for the table headers
                contentStream.setNonStrokingColor(255, 0, 0); // Red color for the header

                // Write table headers
                contentStream.showText("Date:           Type:                         Description:               Statut:"); // Update header accordingly
                contentStream.newLineAtOffset(-10, -20);

                // Reset color to black for the rest of the content
                contentStream.setNonStrokingColor(0, 0, 0);

                // Write an extra newline for space between headers and data
                contentStream.newLineAtOffset(0, -20);

                // Write table data
                for (reclamation rec : data) {
                    contentStream.showText(rec.getDate().toString() +
                            "     " + rec.getType() +
                            "     " + rec.getDecription() +
                            "     " + rec.getStatut());
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.endText(); // Close the text
            }

            document.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    @FXML
    void ShowImage() {
        reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null && selectedReclamation.getImageData() != null) {
            try {
                // Convert Blob to byte array
                byte[] bytes = selectedReclamation.getImageData().getBytes(1, (int) selectedReclamation.getImageData().length());

                // Convert byte array to Image
                Image image = convertToImage(bytes);

                // Display image in a pop-up dialog
                showImageDialog(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Show a pop-up message when there is no image associated with the selected reclamation
            showAlert("Pas de photo associée avec cette réclamation", "Aucune image disponible");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showImageDialog(Image image) {
        Stage imageStage = new Stage();
        imageStage.setTitle("Reclamation Image");

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        VBox vBox = new VBox(new Label("Reclamation Image"), imageView);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff;");

        Scene scene = new Scene(vBox);
        imageStage.setScene(scene);
        imageStage.show();
    }
    private Image convertToImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return new Image(bis);
    }
}
