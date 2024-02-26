package controllers;

import Services.PlatService;
import entities.Plat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import entities.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrontPlatController implements Initializable {

    public ScrollPane scrollp;
    @FXML
    private Label restaurantNameLabel;  // Assume you have a label for the restaurant name

    @FXML
    private GridPane gridp;  // Your GridPane to display plats

    private Restaurant selectedRestaurant;  // Assuming you have a Restaurant class

    public void setRestaurant(Restaurant restaurant) {
        selectedRestaurant = restaurant;
        // You can update your UI elements based on the selected restaurant
        if (restaurant != null) {
            restaurantNameLabel.setText(restaurant.getNom_restaurant());
            // Fetch and display plats for the selected restaurant
            displayPlats(restaurant);
        }
    }

    private void displayPlats(Restaurant restaurant) {
        // Assume you have a PlatService to fetch plats based on the restaurant
        PlatService platService = new PlatService();
        List<Plat> plats = platService.getPlatsForRestaurant(restaurant);

        // Clear existing content from the grid
        gridp.getChildren().clear();

        // Populate the grid with plat information
        int column = 0;
        int row = 0;
        for (Plat plat : plats) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/itemPlat.fxml"));

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                ItemPlatController itemPlatController = fxmlLoader.getController();
                itemPlatController.setData(plat);

                // Add the anchorPane to the grid
                gridp.add(anchorPane, column++, row);

                // Reset column and move to the next row if necessary
                if (column == 1 ) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code...
        // You don't need to create a new GridPane here

        // Example: Fetch and display plats for the selected restaurant
        if (selectedRestaurant != null) {
            restaurantNameLabel.setText(selectedRestaurant.getNom_restaurant());
            displayPlats(selectedRestaurant);
        }

    }}
