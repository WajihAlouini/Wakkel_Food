package com.example.test_javafx2;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.awt.Desktop;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.evaluation;
import service.evaluationservice;
import javafx.scene.text.Text;

import java.sql.Date;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;

public class ListeEvaluationFront {

    @FXML
    private Button surveyButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<evaluation> evaluationsTable;

    @FXML
    private TableColumn<evaluation, Date> dateColumn;

    @FXML
    private TableColumn<evaluation, Integer> noteColumn;

    @FXML
    private TableColumn<evaluation, String> commentaireColumn;

    @FXML
    private Button moyenneButton;

    @FXML
    private Text moyenneText;

    @FXML
    private Button jokeButton;  // Added button for reading a joke

    @FXML
    private Text jokeText;  // Added Text element for displaying a joke

    @FXML
    void initialize() {
        assert evaluationsTable != null : "fx:id=\"evaluationsTable\" was not injected: check your FXML file 'ListeEvaluation.fxml'.";

        // Initialize columns (you can set cell value factories if needed)

        // Populate the TableView
        populateEvaluationsTable();
    }

    // Added method to populate the TableView with evaluation data
    private void populateEvaluationsTable() {
        evaluationservice evaluationservicex = new evaluationservice();
        evaluationsTable.getItems().setAll(evaluationservicex.readAll());
    }

    // ... (existing code)

    @FXML
    private void calculateMoyenne() {
        evaluationservice evaluationservicex = new evaluationservice();
        double moyenne = evaluationservicex.calculateMoyenne(); // Assuming you have a method for this in your service

        // Create an Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Moyenne des évaluations");
        alert.setHeaderText(null);
        alert.setContentText("Moyenne des évaluations : " + moyenne);

        // Show the alert
        alert.showAndWait();
    }

    @FXML
    private void openQRCode() {
        try {
            // Replace the survey link with your actual link
            String surveyLink = "https://fr.surveymonkey.com/r/GNMLNPC";

            // Generate QR code image
            Image qrCodeImage = generateQRCode(surveyLink);

            // Display the QR code
            displayQRCode(qrCodeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image generateQRCode(String text) {
        int width = 300;
        int height = 300;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error generating QR code for URL: " + text);
        }
    }

    private void displayQRCode(Image image) {
        // You can use a new stage or a dialog to display the QR code
        Stage qrCodeStage = new Stage();
        ImageView imageView = new ImageView(image);

        Scene scene = new Scene(new VBox(imageView), 320, 320);
        qrCodeStage.setScene(scene);
        qrCodeStage.setTitle("QR Code");
        qrCodeStage.show();
    }

    @FXML
    private void openSurvey() {
        try {
            // Replace the survey link with your actual link
            URI uri = new URI("https://fr.surveymonkey.com/r/GNMLNPC");
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Added method to handle the "Read a Joke" button action
    @FXML
    private void readJoke() {
        new Thread(() -> {
            try {
                // Replace the joke API URL with the actual URL
                String jokeApiUrl = "https://v2.jokeapi.dev/joke/Any?lang=fr";

                // Send HTTP request to the joke API
                HttpURLConnection connection = (HttpURLConnection) new URL(jokeApiUrl).openConnection();
                connection.setRequestMethod("GET");

                // Check HTTP response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the JSON response to get the joke text
                    String joke = parseJokeFromJson(response.toString());

                    // Display the joke in a pop-up
                    Platform.runLater(() -> showJokePopup(joke));
                } else {
                    System.out.println("HTTP Request Failed with response code: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle errors, display a message, etc.
            }
        }).start();
    }


    private String parseJokeFromJson(String jsonResponse) {
        try {
            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract setup and delivery parts and concatenate them to form the complete joke
            String setup = jsonObject.getString("setup");
            String delivery = jsonObject.getString("delivery");
            return setup + " " + delivery;

        } catch (JSONException e) {
            e.printStackTrace();
            return "Failed to parse joke from API response";
        }
    }


    private void showJokePopup(String joke) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Joke");
        alert.setHeaderText(null);
        alert.setContentText(joke);
        alert.showAndWait();
    }
}
