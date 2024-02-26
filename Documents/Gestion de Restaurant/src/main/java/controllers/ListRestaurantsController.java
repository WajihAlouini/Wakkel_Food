package controllers;
import java.io.IOException;
import java.util.Comparator;

import Services.PlatService;
import entities.Plat;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Restaurant;
import Services.RestaurantService;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.List;
import java.util.Objects;

public class ListRestaurantsController {


    public Button modifierRestoButton;
    public Button supprimerRestoButton;
    public Button ajouteRestoButton;


    @FXML
    private TableColumn<Restaurant, Integer> idColumn;
    @FXML
    private TableView<Restaurant> restaurantsTableView;

    @FXML
    private TableColumn<Restaurant, String> nomColumn;

    @FXML
    private TableColumn<Restaurant, String> adresseColumn;

    @FXML
    private TableColumn<Restaurant, String> categorieColumn;
    @FXML
    private TableColumn<Restaurant, String> imgPathColumn;


    private Stage stage;
    private Scene scene;




    public void initialize() {
        // Set up columns for restaurantsTableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_restaurant"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_restaurant"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse_restaurant"));
        categorieColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurantCategory().getCategoryName()));
        imgPathColumn.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));

        // Fetch the list of restaurants from the database
        RestaurantService restaurantService = new RestaurantService();
        List<Restaurant> restaurants = restaurantService.readAll();

        // Sort the list by ID in ascending order
        restaurants.sort(Comparator.comparingInt(Restaurant::getId_restaurant));

        // Set the sorted list of restaurants to the TableView
        restaurantsTableView.getItems().setAll(restaurants);

        // Set up a listener to handle restaurant selection
        restaurantsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleRestaurantSelection(newValue);
            }
        });

        // Set actions for buttons
        ajouteRestoButton.setOnAction(this::addRestaurant);
        modifierRestoButton.setOnAction(this::modifierRestaurant);
        supprimerRestoButton.setOnAction(this::supprimerRestaurant);
    }



    private void handleRestaurantSelection(Restaurant selectedRestaurant) {
        try {
            // Load the ListPlats.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPlats.fxml"));
            Parent root = loader.load();

            // Access the controller associated with ListPlats.fxml
            ListPlatsController platListController = loader.getController();

            // Pass the selected restaurant to the controller
            platListController.setSelectedRestaurant(selectedRestaurant);

            // Set up the new stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ListPlats.fxml: " + e.getMessage());
        }
    }


    @FXML
    public void addRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading ajoutRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void refreshTableView() {
        // Fetch the list of restaurants from the database
        RestaurantService restaurantService = new RestaurantService();
        List<Restaurant> restaurants = restaurantService.readAll();

        // Sort the list by ID in ascending order
        restaurants.sort(Comparator.comparingInt(Restaurant::getId_restaurant));

        // Set the sorted list of restaurants to the TableView
        restaurantsTableView.getItems().setAll(restaurants);
    }


    @FXML
    public void modifierRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the "Modifier" window
            Stage modifierStage = new Stage();
            modifierStage.setScene(new Scene(root));
            modifierStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading modifierRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void supprimerRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/suppRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the "Supprimer" window
            Stage supprimerStage = new Stage();
            supprimerStage.setScene(new Scene(root));
            supprimerStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading suppRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }











}
