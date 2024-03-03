package controllers;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.application.Platform;

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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PlatController implements Initializable {
    @FXML
    public Button ajoutPlatButton;

    private final ObservableList<Plat> platData = FXCollections.observableArrayList();
    @FXML
    public Button modifierPlatButton;
    @FXML
    public Button supprimerPlatButton;

    public ImageView supplatbg;
    @FXML
    private ImageView imageView;
    @FXML
    private Label NomPlatLabel;
    @FXML
    private Label PrixPlatLabel;
    @FXML
    private Label IngredientsPlatLabel;
    @FXML
    private Label NomRestoPlatLabel;
    @FXML
    private Label ImagePlatLabel;

    @FXML
    private ComboBox<Integer>platIdTextField;

    @FXML
    private TextField platNameTextField;

    @FXML
    private TextField imgPathTextField;

    @FXML
    private TextField prixDuPlatTextField;

    @FXML
    private TextArea ingredientsTextArea;

    @FXML
    private ImageView ajoutplatbackgroundImageView;

    @FXML
    private ComboBox<Restaurant> restaurantComboBox;
    private Stage stage;

    public PlatController() {}





    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load background image
            Image ajoutplatbackgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ajoutplatbackground.jpg")));
            if (ajoutplatbackgroundImageView != null) {
                ajoutplatbackgroundImageView.setImage(ajoutplatbackgroundImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading background image: " + e.getMessage());
        }

        // Check if platIdTextField exists in the current FXML
        if (platIdTextField != null) {
            PlatService platService = new PlatService();

            try {
                // Fetch plat IDs from the database
                List<Plat> plats = platService.readAll();

                // Print debugging information
                System.out.println("Number of plats fetched: " + plats.size());

                if (!plats.isEmpty()) {
                    // Initialize the ComboBox with options
                    List<Integer> platIds = plats.stream().map(Plat::getId_plat).collect(Collectors.toList());
                    ObservableList<Integer> platOptions = FXCollections.observableArrayList(platIds);
                    // Set items to the ComboBox
                    platIdTextField.setItems(platOptions);

                    // Set up a StringConverter to display plat IDs in the ComboBox
                    platIdTextField.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(Integer platId) {
                            return platId != null ? String.valueOf(platId) : "";
                        }

                        @Override
                        public Integer fromString(String string) {
                            // You may need to implement this method if needed
                            return null;
                        }
                    });

                    // Additional debugging: Print the IDs of the first and last plats
                    System.out.println("First Plat ID: " + plats.get(0).getId_plat());
                    System.out.println("Last Plat ID: " + plats.get(plats.size() - 1).getId_plat());

                    // Add any additional initialization logic here
                } else {
                    System.out.println("No plats found in the database.");
                }
            } catch (Exception e) {
                // Handle exceptions gracefully (e.g., log the exception)
                e.printStackTrace();
            }
        } else {
            System.out.println("platIdTextField not found in the FXML.");
        }

        // Check if restaurantComboBox exists in the current FXML
        if (restaurantComboBox != null) {
            RestaurantService restaurantService = new RestaurantService();

            try {
                // Fetch restaurants from the database
                List<Restaurant> restaurants = restaurantService.readAll();

                // Print debugging information
                System.out.println("Number of restaurants fetched: " + restaurants.size());

                if (!restaurants.isEmpty()) {
                    // Initialize the ComboBox with options
                    ObservableList<Restaurant> restaurantOptions = FXCollections.observableArrayList(restaurants);

                    // Set items to the ComboBox
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

                    // Additional debugging: Print the names of the first and last restaurants
                    System.out.println("First Restaurant Name: " + restaurants.get(0).getNom_restaurant());
                    System.out.println("Last Restaurant Name: " + restaurants.get(restaurants.size() - 1).getNom_restaurant());

                    // Add any additional initialization logic here
                } else {
                    System.out.println("No restaurants found in the database.");
                }
            } catch (Exception e) {
                // Handle exceptions gracefully (e.g., log the exception)
                e.printStackTrace();
            }
        } else {
            System.out.println("restaurantComboBox not found in the FXML.");
        }
    }




    private void displayValidationError(Control inputControl, String errorMessage) {
        // Clear existing styles
        inputControl.getStyleClass().remove("error-border");

        if (inputControl instanceof TextInputControl) {
            // Set red border for TextInputControl
            inputControl.getStyleClass().add("error-border");

            // Set the error message as prompt text and placeholder
            ((TextInputControl) inputControl).setPromptText(errorMessage);
            ((TextInputControl) inputControl).setText("");  // Clear the existing text

            // Set prompt text fill to red
            ((TextInputControl) inputControl).setStyle("-fx-prompt-text-fill: red;");
        } else if (inputControl instanceof ComboBox) {
            // Set red text color and border color for ComboBox
            inputControl.setStyle("-fx-text-fill: red; -fx-border-color: red;");
        }
    }





    private void clearValidationError(Control inputControl) {
        if (inputControl instanceof TextInputControl) {
            // Clear border color for TextInputControl
            ((TextInputControl) inputControl).setStyle("-fx-border-color: null;");

            // Clear Tooltip to remove error message
            Tooltip.uninstall(inputControl, ((TextInputControl) inputControl).getTooltip());
        } else if (inputControl instanceof ComboBox) {
            // Clear border color for ComboBox
            ((ComboBox<?>) inputControl).setStyle("-fx-border-color: null;");

            // Clear Tooltip for ComboBox
            Tooltip.uninstall(inputControl, ((ComboBox<?>) inputControl).getTooltip());

            // Clear the error style for ComboBox arrow button
            Node arrowButton = inputControl.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setStyle("-fx-border-color: null;");
            }
        }
    }

    private void updateTextProperty(Control inputControl, String text) {
        if (inputControl instanceof TextInputControl) {
            // Update the text property for TextInputControl
            ((TextInputControl) inputControl).setText(text);
        } else if (inputControl instanceof ComboBox) {
            // Update the text property for ComboBox
            ((ComboBox<?>) inputControl).getEditor().setText(text);
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
        if (platName.isEmpty() && isValidPlatName(platName)) {
            // Display error and return

            displayValidationError(platNameTextField, "Invalid plat name. Please enter a valid name.");
            return;
        } else {
            clearValidationError(platNameTextField);
        }

        double prixDuPlat;
        try {
            prixDuPlat = Double.parseDouble(prixDuPlatText);

            // Check if prixDuPlat is less than 0
            if (prixDuPlat < 0) {
                displayValidationError(prixDuPlatTextField, "Invalid price. Please enter a non-negative value.");
                return;
            } else {
                clearValidationError(prixDuPlatTextField);
            }
        } catch (NumberFormatException e) {
            // Check if prixDuPlat contains letters
            if (prixDuPlatText.matches(".*[a-zA-Z].*")) {
                displayValidationError(prixDuPlatTextField, "Invalid price format. Please enter a valid number.");
                return;
            }

            // Display error for other invalid number formats
            displayValidationError(prixDuPlatTextField, "Invalid price format. Please enter a valid number.");
            return;
        }

        if (ingredients.isEmpty()) {
            displayValidationError(ingredientsTextArea, "Please enter ingredients for the plat.");
            return;
        } else {
            clearValidationError(ingredientsTextArea);
            System.out.println("Plat name validation passed");
        }

        if (selectedRestaurant == null) {
            displayValidationError(restaurantComboBox, "Please select a restaurant for the plat.");
            return;
        } else {
            clearValidationError(restaurantComboBox);
        }

        if (imagePath.isEmpty()) {
            displayValidationError(imgPathTextField, "Image path is required.");
            return;
        } else {
            clearValidationError(imgPathTextField);
        }

        // Create a new Plat object with the collected data
        Plat newPlat = new Plat();
        newPlat.setNomPlat(platName);
        newPlat.setPrix(prixDuPlat);
        newPlat.setIngredient(ingredients);
        newPlat.setRestaurant(selectedRestaurant);
        newPlat.setPimgSrc(imagePath);

        // Use your service class to add the new plat to the database
        PlatService platService = new PlatService();
        platService.add(newPlat);

        // Optionally, clear input fields or perform any other actions after submission


        // Add any additional logic or feedback for the user
        System.out.println("Plat submitted successfully!");
    }
    private void clearFields() {
        platIdTextField.setValue(null);
        platNameTextField.clear();
        prixDuPlatTextField.clear();
        ingredientsTextArea.clear();
        restaurantComboBox.setValue(null);
        imgPathTextField.clear();
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
    private boolean isValidIngredients(String ingredients) {
        // Matches if the string is empty or contains only letters and/or spaces
        return ingredients.isEmpty() || ingredients.matches("[a-zA-Z\\s]+");
    }


    private Stage originalStage;

    @FXML
    private void handleModifierPlatButton(ActionEvent event) {
        // Retrieve data from input fields
        String platIdText = String.valueOf(platIdTextField.getValue().intValue());
        String platName = platNameTextField.getText();
        String prixDuPlatText = prixDuPlatTextField.getText();
        String ingredients = ingredientsTextArea.getText();
        Restaurant selectedRestaurant = restaurantComboBox.getValue();
        String imagePath = imgPathTextField.getText();

        // Validate data (similar to the validation in handleModifier)
        clearValidationError(platIdTextField);

        // Add custom validation for the plat name (letters and spaces only)
        if (isValidPlatName(platName)) {
            displayValidationError(platNameTextField, "Nom du plat should contain only letters and spaces.");
            return;
        } else {
            clearValidationError(platNameTextField);
        }

        // Validate prixDuPlat field (numeric and non-negative)
        double prixDuPlat1;
        try {
            prixDuPlat1 = Double.parseDouble(prixDuPlatText);

            // Check if prixDuPlat is less than 0
            if (prixDuPlat1 < 0) {
                displayValidationError(prixDuPlatTextField, "Invalid price. Please enter a non-negative value.");
                return;
            } else {
                clearValidationError(prixDuPlatTextField);
            }
        } catch (NumberFormatException e) {
            // Check if prixDuPlat contains letters
            if (prixDuPlatText.matches(".*[a-zA-Z].*")) {
                displayValidationError(prixDuPlatTextField, "Invalid price format. Please enter a valid number.");
                return;
            }

            // Display error for other invalid number formats
            displayValidationError(prixDuPlatTextField, "Invalid price format. Please enter a valid number.");
            return;
        }

        if (!isValidIngredients(ingredients)) {
            displayValidationError(ingredientsTextArea, "Please enter valid ingredients for the plat.");
            return;
        } else {
            clearValidationError(ingredientsTextArea);
        }

        if (selectedRestaurant == null) {
            displayValidationError(restaurantComboBox, "Please select a restaurant for the plat.");
            return;
        } else {
            clearValidationError(restaurantComboBox);
        }

        if (imagePath.isEmpty()) {
            displayValidationError(imgPathTextField, "Image path is required.");
            return;
        } else {
            clearValidationError(imgPathTextField);
        }

        // Continue with other validations if needed...

        // Convert platIdText to int
        int platId;
        try {
            platId = Integer.parseInt(platIdText);
        } catch (NumberFormatException e) {
            displayValidationError(platIdTextField, "Invalid plat ID format. Please enter a valid number.");
            return;
        }

        // Create a new Plat object with the collected data
        Plat modifiedPlat = new Plat();
        modifiedPlat.setId_plat(platId);
        modifiedPlat.setNomPlat(platName);
        modifiedPlat.setPrix(prixDuPlat1);
        modifiedPlat.setIngredient(ingredients);
        modifiedPlat.setRestaurant(selectedRestaurant);
        modifiedPlat.setPimgSrc(imagePath);

        // Use your service class to update the modified plat in the database
        PlatService platService = new PlatService();
        platService.update(modifiedPlat);

        // Optionally, clear input fields or perform any other actions after modification
        clearFields();

        // Add any additional logic or feedback for the user
        System.out.println("Plat modified successfully!");
    }

    @FXML
    private void handleSupprimerPlat() {
        // Retrieve data from input field
        String platId = platIdTextField.getValue().toString();

        // Validate data
        if (platId.isEmpty()) {
            displayValidationError(platIdTextField, "ID du plat is required.");
            return;
        } else {
            clearValidationError(platIdTextField);
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
            platIdTextField.setValue(null);

            // Close the current window
            Stage currentStage = (Stage) platIdTextField.getScene().getWindow();
            currentStage.close();

            // Show the ListPlats.fxml window


            // Add any additional logic or feedback for the user
            System.out.println("Plat supprimé avec succès!");

        } catch (NumberFormatException e) {
            displayValidationError(platIdTextField, "Invalid plat ID format. Please enter a valid number.");
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
    }@FXML
    private void handleChooseImageButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Update the TextField with the selected file path
        if (selectedFile != null) {
            // Check if the selected file has a valid image file extension
            if (isValidImageFile(selectedFile)) {
                imgPathTextField.setText(selectedFile.getAbsolutePath());

                // Dynamically determine the XAMPP installation directory
                String xamppInstallationDirectory = System.getenv("XAMPP_HOME");
                if (xamppInstallationDirectory == null) {
                    // If XAMPP_HOME environment variable is not set, you may use a default path
                    xamppInstallationDirectory = "A:/xampp";
                }

                // Construct the target directory path
                Path targetDirectory = Paths.get(xamppInstallationDirectory, "htdocs", "plat_images");

                // Move the selected file to the target directory
                try {
                    Files.createDirectories(targetDirectory);  // Create directory if not exists
                    Path targetPath = targetDirectory.resolve(selectedFile.getName());
                    Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // Construct the URL for the image
                    String imageUrl = "http://localhost/plat_images/" + selectedFile.getName();

                    // Update the image in your JavaFX application
                    Image image = new Image(imageUrl);

                    // Check if imageView is not null before setting the image
                    if (imageView != null) {
                        // Use Platform.runLater if calling from a background thread
                        Platform.runLater(() -> imageView.setImage(image));
                    } else {
                        // Handle the case where imageView is null (e.g., show an error message)
                        System.err.println("ImageView is null. Cannot update the image.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception (e.g., show an error message)
                }
            } else {
                // Handle the case where the selected file is not a valid image file
                // You may show an error message to the user
                System.err.println("Selected file is not a valid image file.");
            }
        }
    }


    private boolean isValidImageFile(File file) {
        // Add your logic to check if the file is a valid image file
        // For example, you can check the file extension
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
    }
    private void handleSearch(String keyword) {
        List<Plat> searchResults = PlatService.searchPlats (keyword);
        platData.setAll(searchResults);
    }

    }
