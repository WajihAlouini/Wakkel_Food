package com.example.test_javafx2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.evaluation;
import service.evaluationservice;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

public class ListeEvaluation {

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

    @FXML
    private void ExportPdf() {
        ObservableList<evaluation> data = evaluationsTable.getItems();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Choose a different font, adjust the font size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

                // Write the document title in black
                contentStream.setNonStrokingColor(0, 0, 0); // Black color for the title
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Liste des évaluations pour le " + java.time.LocalDate.now());
                contentStream.newLineAtOffset(0, -30);

                // Reset color to red for the table headers
                contentStream.setNonStrokingColor(255, 0, 0); // Red color for the header

                // Write table headers
                contentStream.showText("Date:     Note:     Commentaire:");
                contentStream.newLineAtOffset(-10, -20);

                // Reset color to black for the rest of the content
                contentStream.setNonStrokingColor(0, 0, 0);

                // Write an extra newline for space between headers and data
                contentStream.newLineAtOffset(0, -20);

                // Write table data
                for (evaluation evaluation : data) {
                    contentStream.showText(evaluation.getDate().toString() +
                            "     " + evaluation.getNote() +
                            "     " + evaluation.getCommentaire());
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.endText(); // Close the text
            }

            // Generate a filename with a counter
            int counter = 1;
            File file;
            do {
                file = new File("Evaluation_export_" + counter + ".pdf");
                counter++;
            } while (file.exists());

            document.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }







    @FXML
    private void DeleteEvaluation() {
        evaluationservice evaluationservicex = new evaluationservice();
        evaluation selectedEvaluation = evaluationsTable.getSelectionModel().getSelectedItem();

        if (selectedEvaluation != null) {
            evaluationservicex.delete(selectedEvaluation.getId_evaluation());
            populateEvaluationsTable(); // Refresh the TableView after deletion
        }
    }

    @FXML
    private void UpdateEvaluation() {
        evaluation selectedEvaluation = evaluationsTable.getSelectionModel().getSelectedItem();

        if (selectedEvaluation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvaluation.fxml"));
                AnchorPane root = loader.load();

                com.example.test_javafx2.ModifierEvaluationController modifierController = loader.getController();
                modifierController.setEvaluation(selectedEvaluation);

                Stage updateStage = new Stage();
                updateStage.initModality(Modality.WINDOW_MODAL);
                updateStage.initOwner(evaluationsTable.getScene().getWindow());
                updateStage.setScene(new Scene(root));

                updateStage.showAndWait();

                populateEvaluationsTable();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }

    }
}
