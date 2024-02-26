package controllers;

import Services.PlatService;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListPlatsController {
    public Restaurant selectedRestaurant;
    @FXML
    private TableView<Plat> platsTableView;

    @FXML
    private TableColumn<Plat, Integer> idColumn1;

    @FXML
    private TableColumn<Plat, String> nomColumn1;

    @FXML
    private TableColumn<Plat, Double> prixColumn;

    @FXML
    private TableColumn<Plat, String> ingredientColumn;

    @FXML
    private Button ajouterPlatButton;

    @FXML
    private Button modifierPlatButton;

    @FXML
    private Button supprimerPlatButton;

    private Stage stage;
    private Scene scene;

    // Reference to the selected restaurant


    public void setSelectedRestaurant(Restaurant selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
        // Update the platsTableView with the plats of the selected restaurant
        refreshTableView();
    }

    @FXML
    public void addPlat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutPlat.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading ajoutPlat.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void modifierPlat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPlat.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading modifierPlat.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void supprimerPlat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/supprimerPlat.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading supprimerPlat.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    public void initialize() {
        // Set up columns for platsTableView
        idColumn1.setCellValueFactory(new PropertyValueFactory<>("id_plat"));
        nomColumn1.setCellValueFactory(new PropertyValueFactory<>("nom_plat"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

        // Set up a listener to handle plat selection (if needed)
        platsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handlePlatSelection(newValue);
            }
        });
    }

    private void handlePlatSelection(Plat selectedPlat) {
        // Handle the selection of a plat, if needed
        // For example, you can open a new window to display more details about the selected plat
    }

    private void refreshTableView() {
        // Fetch the updated list of plats for the selected restaurant from the database
        if (selectedRestaurant != null) {
            PlatService platService = new PlatService();
            List<Plat> plats = platService.getPlatsByRestaurantId(selectedRestaurant.getId_restaurant());

            // Set the sorted list of plats to the TableView
            updateData(plats);
        }
    }

    private void updateData(List<Plat> plats) {
        // Update the data in platsTableView
        ObservableList<Plat> platObservableList = FXCollections.observableArrayList(plats);
        platsTableView.setItems(platObservableList);
    }public void setStage(Stage stage) {
        this.stage = stage;
    }

}
