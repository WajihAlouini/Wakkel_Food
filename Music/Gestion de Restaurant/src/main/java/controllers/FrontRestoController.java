package controllers;

import Services.RestaurantService;
import entities.Restaurant;
import entities.RestaurantCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FrontRestoController implements Initializable {

    @FXML
    private Label CategoryLabel;

    @FXML
    private Label NameLabel;

    @FXML
    private ImageView img;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;
    private final RestaurantService restaurantService = new RestaurantService();

    private List<Restaurant> getRestaurants() {
        RestaurantService restaurantService = new RestaurantService();
        return restaurantService.readAll();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int column = 0;
        int row = 0;

        // Fetch the restaurant list from the service
        List<Restaurant> restaurants = restaurantService.readAll();

        try {
            for (Restaurant restaurant : restaurants) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/itemResto.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();
                ItemRestoController itemRestoController = fxmlLoader.getController();
                itemRestoController.setData(restaurant);
                anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> openPlatGrid(restaurant));

                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void openPlatGrid(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontPlat.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            FrontPlatController frontPlatController = loader.getController();
            frontPlatController.setRestaurant(restaurant);

            stage.showAndWait();  // Wait for the plat grid window to be closed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleRestaurantClick(MouseEvent event) {
        // Assuming the grid contains items representing restaurants,
        // and each item has an associated FXML file (e.g., frontPlat.fxml)

        try {
            // Identify which restaurant was clicked
            // You can get the clicked item from the event and extract information as needed

            // Load frontPlat.fxml or another appropriate FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("frontPlat.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Wakkel food - Plat"); // Set the title for the new stage
            stage.show();

            // You can close the previous stage if needed
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
