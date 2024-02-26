package controllers;

import Services.RestaurantCategoryService;
import Services.RestaurantService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import entities.Restaurant;
import entities.RestaurantCategory;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantController implements javafx.fxml.Initializable {
    public Button supprimerButton;
    private Stage stage;
     private Scene scene;

    @FXML
    private ComboBox<RestaurantCategory> categoryComboBox;
    @FXML
    private TextField idTextField;
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




    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RestaurantCategoryService categoryService = new RestaurantCategoryService();

        try {
            // Fetch categories from the database
            List<RestaurantCategory> categories = categoryService.readAll();

            // Initialize the ComboBox with options
            ObservableList<RestaurantCategory> categoryOptions = FXCollections.observableArrayList(categories);
            categoryComboBox.setItems(categoryOptions);

            // Set up a StringConverter to display category names in the ComboBox
            categoryComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(RestaurantCategory category) {
                    return category != null ? category.getCategoryName() : "";
                }

                @Override
                public RestaurantCategory fromString(String string) {
                    // You may need to implement this method if needed
                    return null;
                }
            });



            // Set the action for the ajouterButton
            ajouterButton1.setOnAction(event -> handleAjouter());
            modifierButton.setOnAction(event -> handleModifier());
            supprimerButton.setOnAction(event->handleSupprimer());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching and initializing categories.");
        }
    }

    private boolean isValidRestaurantName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    @FXML
    private void handleAjouter() {
        // Retrieve data from input fields
        String restaurantName = nameTextField.getText();
        String restaurantAddress = addressTextField.getText();
        RestaurantCategory selectedCategory = categoryComboBox.getValue();
        String imagePath = imagePathField.getText();

        // Validate data
        if (restaurantName.isEmpty()) {
            System.out.println("Nom du restaurant is required.");
            return;
        }

        // Add custom validation for the restaurant name (letters and spaces only)
        if (!isValidRestaurantName(restaurantName)) {
            System.out.println("Nom du restaurant should contain only letters and spaces.");
            return;
        }

        // Validate address field (not empty)
        if (restaurantAddress.isEmpty()) {
            System.out.println("Adresse is required.");
            return;
        }

        // Validate that a category is selected
        if (selectedCategory == null) {
            System.out.println("Please select a restaurant category.");
            return;
        }

        // Validate image path (not empty)
        if (imagePath.isEmpty()) {
            System.out.println("Image path is required.");
            return;
        }

        // Continue with other validations if needed...

        // Create a new Restaurant object with the collected data
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setNom_restaurant(restaurantName);
        newRestaurant.setAdresse_restaurant(restaurantAddress);
        newRestaurant.setRestaurantCategory(selectedCategory);
        newRestaurant.setImgSrc(imagePath); // Set the image path

        // Use your service class to add the new restaurant to the database
        RestaurantService restaurantService = new RestaurantService();
        restaurantService.add(newRestaurant);

        // Optionally, clear input fields or perform any other actions after submission
        nameTextField.clear();
        addressTextField.clear();
        categoryComboBox.setValue(null);
        imagePathField.clear(); // Clear the image path field

        // Add any additional logic or feedback for the user
        System.out.println("Restaurant submitted successfully!");
        // You can also display a success message to the user using a Label or other UI element
    }







    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleModifier() {
        // Retrieve data from input fields
        String restaurantId = idTextField.getText();
        String restaurantName = nameTextField.getText();
        String restaurantAddress = addressTextField.getText();
        RestaurantCategory selectedCategory = categoryComboBox.getValue();
        String imagePath = imagePathField.getText(); // Get the updated image path

        // Validate data, you can customize the validation based on your requirements
        if (restaurantId.isEmpty()) {
            System.out.println("ID du restaurant is required.");
            return;
        }

        // Add custom validation for the restaurant name (letters and spaces only)
        if (!isValidRestaurantName(restaurantName)) {
            System.out.println("Nom du restaurant should contain only letters and spaces.");
            return;
        }

        // Validate address field (not empty)
        if (restaurantAddress.isEmpty()) {
            System.out.println("Adresse is required.");
            return;
        }

        // Validate that a category is selected
        if (selectedCategory == null) {
            System.out.println("Please select a restaurant category.");
            return;
        }

        // Continue with other validations if needed...

        // Create a new Restaurant object with the collected data
        Restaurant modifiedRestaurant = new Restaurant();
        modifiedRestaurant.setId_restaurant(Integer.parseInt(restaurantId)); // Assuming ID is an integer
        modifiedRestaurant.setNom_restaurant(restaurantName);
        modifiedRestaurant.setAdresse_restaurant(restaurantAddress);
        modifiedRestaurant.setRestaurantCategory(selectedCategory);
        modifiedRestaurant.setImgSrc(imagePath); // Set the updated image path

        // Use your service class to update the restaurant in the database
        RestaurantService restaurantService = new RestaurantService();
        restaurantService.update(modifiedRestaurant);

        // Optionally, clear input fields or perform any other actions after modification
        idTextField.clear();
        nameTextField.clear();
        addressTextField.clear();
        categoryComboBox.setValue(null);
        imagePathField.clear(); // Clear the image path field

        // Add any additional logic or feedback for the user
        System.out.println("Restaurant modified successfully!");
    }

    @FXML
    private void handleSupprimer() {
        // Retrieve data from the input field (ID)
        String restaurantId = idTextField.getText();

        // Validate data, you can customize the validation based on your requirements
        if (restaurantId.isEmpty()) {
            System.out.println("ID du restaurant is required.");
            return;
        }

        try {
            // Use your service class to delete the restaurant from the database
            int idRestaurant = Integer.parseInt(restaurantId);
            RestaurantService restaurantService = new RestaurantService();
            restaurantService.delete(idRestaurant);

            // Optionally, clear input fields or perform any other actions after deletion
            idTextField.clear();


            // Add any additional logic or feedback for the user
            System.out.println("Restaurant supprimé avec succès!");
        } catch (NumberFormatException e) {
            System.out.println("ID du restaurant doit être un nombre entier.");
        }
    }

}
