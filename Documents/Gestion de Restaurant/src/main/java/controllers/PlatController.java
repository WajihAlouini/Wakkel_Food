package controllers;

import Services.*;
import entities.Plat;
import entities.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PlatController {
    @FXML
    public Button ajoutPlatButton;
    @FXML
    public Button modifierPlatButton;
    @FXML
    public Button supprimerPlatButton;


    @FXML
    private TextField platIdTextField;

    @FXML
    private TextField platNameTextField;

    @FXML
    private TextField imgPathTextField;

    @FXML
    private TextField prixDuPlatTextField;

    @FXML
    private TextArea ingredientsTextArea;

    @FXML
    private ComboBox<Restaurant> restaurantComboBox;
    private Stage stage;

    public PlatController() {
    }


    @FXML
    private void initialize() {
        // Initialize the form, e.g., populate the restaurant ComboBox
        initForm();
    }

    private void initForm() {
        // Check if restaurantComboBox exists in the current FXML
        if (restaurantComboBox != null) {
            RestaurantService restaurantService = new RestaurantService();

            // Fetch restaurants from the database
            List<Restaurant> restaurants = restaurantService.readAll();

            // Initialize the ComboBox with options
            ObservableList<Restaurant> restaurantOptions = FXCollections.observableArrayList(restaurants);
            restaurantComboBox.setItems(restaurantOptions);

            // Set up a StringConverter to display restaurant names in the ComboBox
            restaurantComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Restaurant restaurant) {
                    return restaurant != null ? restaurant.getNom_restaurant() : "";
                }

                @Override
                public Restaurant fromString(String string) {
                    // You may need to implement this method if needed
                    return null;
                }
            });

            // Add any additional initialization logic here
        }
    }


    @FXML
    private void handleAjoutPlatButton(ActionEvent event) {
        // Retrieve data from input fields
        String platName = platNameTextField.getText();
        String prixDuPlatText = prixDuPlatTextField.getText();
        String ingredients = ingredientsTextArea.getText();
        Restaurant selectedRestaurant = restaurantComboBox.getValue();
        String imagePath = imgPathTextField.getText();

        // Validate data
        if (platName.isEmpty() || isValidPlatName(platName)) {
            // Display an error message for invalid plat name
            System.out.println("Invalid plat name. Please enter a valid name.");
            return;
        }

        double prixDuPlat;
        try {
            prixDuPlat = Double.parseDouble(prixDuPlatText);
            if (prixDuPlat < 0) {
                // Display an error message for negative price
                System.out.println("Invalid price. Please enter a non-negative value.");
                return;
            }
        } catch (NumberFormatException e) {
            // Display an error message for invalid price format
            System.out.println("Invalid price format. Please enter a valid number.");
            return;
        }

        if (ingredients.isEmpty()) {
            // Display an error message for empty ingredients
            System.out.println("Please enter ingredients for the plat.");
            return;
        }
        // Validate image path (not empty)
        if (imagePath.isEmpty()) {
            System.out.println("Image path is required.");
            return;
        }

        // Create a new Plat object with the collected data
        Plat newPlat = new Plat();
        newPlat.setNomPlat(platName);
        newPlat.setPrix(prixDuPlat);
        newPlat.setIngredient(ingredients);
        newPlat.setRestaurant(selectedRestaurant);

        // Use your service class to add the new plat to the database
        PlatService platService = new PlatService();
        platService.add(newPlat);

        // Optionally, clear input fields or perform any other actions after submission
        platNameTextField.clear();
        prixDuPlatTextField.clear();
        ingredientsTextArea.clear();
        restaurantComboBox.setValue(null); // Clear the selected restaurant

        // Add any additional logic or feedback for the user
        System.out.println("Plat submitted successfully!");

        // Close the current window (PlatController)
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

        // Open the ListPlatsController window
        openListPlatsWindow(selectedRestaurant);
    }



    private void openListPlatsWindow(Restaurant selectedRestaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPlats.fxml"));
            Parent root = loader.load();

            // Access the controller associated with ListPlats.fxml
            ListPlatsController listPlatsController = loader.getController();

            // Pass the selected restaurant to the controller
            listPlatsController.setSelectedRestaurant(selectedRestaurant);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }



    // Validate plat name (does not contain numbers or special symbols)
    private boolean isValidPlatName(String platName) {
        return !platName.matches("[a-zA-Z\\s]+");
    }

    private Stage originalStage;

    @FXML
    private void handleModifierPlatButton(ActionEvent event) {
        // Retrieve data from input fields
        String platIdText = platIdTextField.getText();
        String platName = platNameTextField.getText();
        String prixDuPlatText = prixDuPlatTextField.getText();
        String ingredients = ingredientsTextArea.getText();
        Restaurant selectedRestaurant = restaurantComboBox.getValue();
        String imagePath = imgPathTextField.getText();

        // Validate data (similar to the validation in handleModifier)
        if (platIdText.isEmpty()) {
            System.out.println("ID du plat is required.");
            return;
        }

        // Add custom validation for the plat name (letters and spaces only)
        if (isValidPlatName(platName)) {
            System.out.println("Nom du plat should contain only letters and spaces.");
            return;
        }

        // Validate prixDuPlat field (numeric and non-negative)
        double prixDuPlat;
        try {
            prixDuPlat = Double.parseDouble(prixDuPlatText);
            if (prixDuPlat < 0) {
                System.out.println("Invalid price. Please enter a non-negative value.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a valid number.");
            return;
        }

        // Validate ingredients field (not empty)
        if (ingredients.isEmpty()) {
            System.out.println("Please enter ingredients for the plat.");
            return;
        }

        // Validate that a restaurant is selected
        if (selectedRestaurant == null) {
            System.out.println("Please select a restaurant for the plat.");
            return;
        }
        if (imagePath.isEmpty()) {
            System.out.println("Image path is required.");
            return;
        }
        // Continue with other validations if needed...

        // Convert platIdText to int
        int platId;
        try {
            platId = Integer.parseInt(platIdText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid plat ID format. Please enter a valid number.");
            return;
        }

        // Create a new Plat object with the collected data
        Plat modifiedPlat = new Plat();
        modifiedPlat.setId_plat(platId);
        modifiedPlat.setNomPlat(platName);
        modifiedPlat.setPrix(prixDuPlat);
        modifiedPlat.setIngredient(ingredients);
        modifiedPlat.setRestaurant(selectedRestaurant);

        // Use your service class to update the modified plat in the database
        PlatService platService = new PlatService();
        platService.update(modifiedPlat);

        // Optionally, clear input fields or perform any other actions after modification
        platIdTextField.clear();
        platNameTextField.clear();
        prixDuPlatTextField.clear();
        ingredientsTextArea.clear();
        restaurantComboBox.setValue(null);

        // Navigate back to ListPlats.fxml
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ListPlats.fxml")));

            // Set up the new stage and scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Close the original window
            originalStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Add any additional logic or feedback for the user
        System.out.println("Plat modified successfully!");
    }

    @FXML
    private void handleSupprimerPlat() {
        // Retrieve data from input field
        String platId = platIdTextField.getText();

        // Validate data
        if (platId.isEmpty()) {
            System.out.println("ID du plat is required.");
            return;
        }

        // Continue with other validations if needed...

        // Use your service class to delete the plat from the database
        PlatService platService = new PlatService();

        try {
            // Parse the platId to an integer
            int idPlat = Integer.parseInt(platId);

            // Delete the plat
            platService.delete(idPlat);

            // Optionally, clear input fields or perform any other actions after deletion
            platIdTextField.clear();

            // Close the current window
            Stage currentStage = (Stage) platIdTextField.getScene().getWindow();
            currentStage.close();

            // Show the ListPlats.fxml window
            showListPlatsWindow();

            // Add any additional logic or feedback for the user
            System.out.println("Plat supprimé avec succès!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid plat ID format. Please enter a valid number.");
        } catch (Exception e) {
            // Handle other exceptions if needed
            e.printStackTrace();
        }
    }

    private void showListPlatsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPlats.fxml"));
            Parent root = loader.load();

            // Set up the new stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the ListPlats.fxml window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public Restaurant getSelectedRestaurant() {
        return new Restaurant();
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
