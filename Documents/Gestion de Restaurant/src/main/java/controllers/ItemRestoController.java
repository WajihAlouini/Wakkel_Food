package controllers;

import entities.Restaurant;
import entities.RestaurantCategory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.InputStream;

public class ItemRestoController {

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
    private Restaurant restaurant;
    public void setData(Restaurant restaurant) {
        // Set the image, name, and category for the restaurant
        String imgSrc = restaurant.getImgSrc();

        if (imgSrc != null && !imgSrc.isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream(imgSrc);

            if (imageStream != null) {
                Image image = new Image(imageStream);
                img.setImage(image);
            } else {
                // Handle the case where the image is not found
                System.err.println("Image not found: " + imgSrc);
            }
        } else {
            // Handle the case where imgSrc is null or empty
            // You can set a default image or take appropriate action.
            System.err.println("Image source is null or empty for restaurant: " + restaurant.getNom_restaurant());
        }

        NameLabel.setText(restaurant.getNom_restaurant());

        // Set the category label
        RestaurantCategory restaurantCategory = restaurant.getRestaurantCategory();
        if (restaurantCategory != null) {
            CategoryLabel.setText(restaurantCategory.getCategoryName());
        } else {
            // Handle the case where the category is null
            CategoryLabel.setText("Unknown Category");
        }
    }






}
