package org.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFx extends Application {

    private Stage primaryStage;
    private static final Logger logger = Logger.getLogger(MainFx.class.getName());

    private void loadFXML(String fxmlFile) throws IOException {
        try {
            // Use the ClassLoader to load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
            Parent root = loader.load();


            initializeScene(root);

            // Set the scene to the primary stage
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.setTitle("Wakkel food");
            this.primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading " + fxmlFile, e);
            // Handle the exception gracefully, e.g., show an error dialog
            throw new IOException("Error loading " + fxmlFile, e); // rethrow the exception to propagate it
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            loadFXML("listrestaurants.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading ListRestaurants.fxml", e);
            // Handle the exception gracefully, e.g., show an error dialog
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeScene(Parent root) {
        // Customize the scene if needed
        // For example, set stylesheets, add global event handlers, etc.
    }
}
