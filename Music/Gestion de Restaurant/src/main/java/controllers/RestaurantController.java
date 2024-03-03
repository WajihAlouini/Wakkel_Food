package controllers;

import Services.RestaurantCategoryService;
import Services.RestaurantService;
import entities.Restaurant;
import entities.RestaurantCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RestaurantController implements javafx.fxml.Initializable {
    @FXML
    private ImageView imageView = new ImageView();

    private final ObservableList<Restaurant> restaurantData = FXCollections.observableArrayList();
    @FXML
    private ComboBox<RestaurantCategory> categoryComboBox;

    @FXML
    private ComboBox<Integer> idTextField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private Button ajouterButton1;
    @FXML
    private Button modifierButton;
    @FXML
    public Button supprimerButton;



    public RestaurantController() {
    }

    @FXML
    public void handleAjouter() {
        String restaurantName = nameTextField.getText();
        String restaurantAddress = addressTextField.getText();
        RestaurantCategory selectedCategory = categoryComboBox.getValue();
        String imagePath = imagePathField.getText(); // Assuming this is the path of the image

        if (restaurantName.isEmpty() || !isValidName(restaurantName)) {
            displayValidationError(nameTextField, "Only letters and spaces are allowed.");
            return;
        } else {
            clearValidationError(nameTextField);
        }

        if (restaurantAddress.isEmpty()) {
            displayValidationError(addressTextField, "Address is required.");
            return;
        } else {
            clearValidationError(addressTextField);
        }

        if (selectedCategory == null) {
            displayValidationError(categoryComboBox, "Please select a restaurant category.");
            return;
        } else {
            clearValidationError(categoryComboBox);
        }

        if (imagePath.isEmpty()) {
            displayValidationError(imagePathField, "Image path is required.");
            return;
        } else {
            clearValidationError(imagePathField);
        }

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setNom_restaurant(restaurantName);
        newRestaurant.setAdresse_restaurant(restaurantAddress);
        newRestaurant.setRestaurantCategory(selectedCategory);

        // Assuming imagePath is the full path of the image
        newRestaurant.setImgSrc(imagePath);

        RestaurantService restaurantService = new RestaurantService();
        restaurantService.add(newRestaurant);

        clearFields();

        System.out.println("Restaurant submitted successfully!");
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z ]+$");
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
            inputControl.setStyle("-fx-prompt-text-fill: red;");
        } else if (inputControl instanceof ComboBox) {
            // Set red text color and border color for ComboBox
            inputControl.setStyle("-fx-text-fill: red; -fx-border-color: red;");
        }
    }






    private void clearValidationError(Control inputControl) {
        inputControl.getStyleClass().removeAll("error-border");
        if (inputControl instanceof TextInputControl) {
            ((TextInputControl) inputControl).setPromptText("");
        } else if (inputControl instanceof ComboBox) {
            inputControl.setStyle("");
        }
    }

    private void clearFields() {
       if(idTextField!=null){ idTextField.setValue(null);}
        nameTextField.clear();
        addressTextField.clear();
        categoryComboBox.setValue(null);
        imagePathField.clear();
    }

    private boolean isValidRestaurantName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    @FXML
    private void handleModifier() {
        String restaurantIdText = String.valueOf(idTextField.getValue().intValue());
        String restaurantName = nameTextField.getText();
        String restaurantAddress = addressTextField.getText();
        RestaurantCategory selectedCategory = categoryComboBox.getValue();
        String imagePath = imagePathField.getText(); // Assuming this is the path of the image

        clearValidationError(idTextField);

        if (!isValidRestaurantName(restaurantName)) {
            displayValidationError(nameTextField, "Only letters and spaces are allowed.");
            return;
        } else {
            clearValidationError(nameTextField);
        }

        if (restaurantAddress.isEmpty()) {
            displayValidationError(addressTextField, "Address is required.");
            return;
        } else {
            clearValidationError(addressTextField);
        }

        if (selectedCategory == null) {
            displayValidationError(categoryComboBox, "Please select a restaurant category.");
            return;
        } else {
            clearValidationError(categoryComboBox);
        }

        int restaurantId;
        try {
            restaurantId = Integer.parseInt(restaurantIdText);
        } catch (NumberFormatException e) {
            displayValidationError(idTextField, "Invalid restaurant ID format. Please enter a valid number.");
            return;
        }

        // Assuming imagePath is the full path of the image

        Restaurant modifiedRestaurant = new Restaurant();
        modifiedRestaurant.setId_restaurant(restaurantId);
        modifiedRestaurant.setNom_restaurant(restaurantName);
        modifiedRestaurant.setAdresse_restaurant(restaurantAddress);
        modifiedRestaurant.setRestaurantCategory(selectedCategory);
        modifiedRestaurant.setImgSrc(imagePath);

        // Assuming imagePath is the full path of the image

        RestaurantService restaurantService = new RestaurantService();
        restaurantService.update(modifiedRestaurant);

        clearFields();

        System.out.println("Restaurant modified successfully!");

        // You may want to remove the following line as it seems misleading
        System.out.println("Image path is empty.");
    }



    private void handleSearch(String keyword) {
        List<Restaurant> searchResults = RestaurantService.searchRestaurants (keyword);
        restaurantData.setAll(searchResults);
    }











    @FXML
    private void handleSupprimer() {
        initializeIdTextField();
        String restaurantId = idTextField.getValue().toString();

        if (restaurantId.isEmpty()) {
            System.out.println("ID du restaurant is required.");
            return;
        }

        try {
            int idRestaurant = Integer.parseInt(restaurantId);
            RestaurantService restaurantService = new RestaurantService();
            restaurantService.delete(idRestaurant);

            idTextField.setValue(null);
            System.out.println("Restaurant supprimé avec succès!");
        } catch (NumberFormatException e) {
            System.out.println("ID du restaurant doit être un nombre entier.");
        }
    }
    private void initializeCategoryComboBox() {
        RestaurantCategoryService categoryService = new RestaurantCategoryService();
        try {
            List<RestaurantCategory> categories = categoryService.readAll();
            ObservableList<RestaurantCategory> categoryOptions = FXCollections.observableArrayList(categories);

            if (categoryComboBox != null) {
                categoryComboBox.setItems(categoryOptions);

                categoryComboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(RestaurantCategory category) {
                        return category != null ? category.getCategoryName() : "";
                    }

                    @Override
                    public RestaurantCategory fromString(String string) {
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching and initializing categories.");
        }
    }

    private void initializeButtons() {
        if (ajouterButton1 != null) {
            ajouterButton1.setOnAction(event -> handleAjouter());
        }

        if (modifierButton != null) {
            modifierButton.setOnAction(event -> handleModifier());
        }

        if (supprimerButton != null) {
            supprimerButton.setOnAction(event -> handleSupprimer());
        }
    }

    // Your other methods (handleAjouter, handleModifier, handleSupprimer, etc.) go here
    private void initializeIdTextField() {
        RestaurantService restaurantService = new RestaurantService();
        try {
            // Fetch restaurant IDs from the database
            List<Restaurant> restaurants = restaurantService.readAll();

            // Print debugging information
            System.out.println("Number of restaurants fetched: " + restaurants.size());

            if (!restaurants.isEmpty()) {
                // Initialize the ComboBox with options
                List<Integer> restaurantIds = restaurants.stream().map(Restaurant::getId_restaurant).collect(Collectors.toList());
                ObservableList<Integer> restaurantOptions = FXCollections.observableArrayList(restaurantIds);

                // Set items to the ComboBox
                idTextField.setItems(restaurantOptions);

                // Set up a StringConverter to display restaurant IDs in the ComboBox
                idTextField.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(Integer restaurantId) {
                        return restaurantId != null ? String.valueOf(restaurantId) : "";
                    }

                    @Override
                    public Integer fromString(String string) {
                        // You may need to implement this method if needed
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching and initializing restaurant IDs.");
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resBundle) {
        initializeIdTextField();
        initializeCategoryComboBox();
        initializeButtons();
    }
    @FXML
    private void chooseImage() {
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
                imagePathField.setText(selectedFile.getAbsolutePath());

                // Construct the relative path from the htdocs directory
                Path relativePath = Paths.get("restaurant_images", selectedFile.getName());

                // Update the image in your JavaFX application
                Image image = new Image("file:" + selectedFile.getAbsolutePath());
                imageView.setImage(image);
            } else {
                // Handle the case where the selected file is not a valid image file
                // You may show an error message to the user
            }
        }
    }




    private boolean isValidImageFile(File file) {
        // Check if the file has a valid image file extension
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
    }
}








