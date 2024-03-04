package com.example.test_javafx2 ;

import entities.Restaurant;
import entities.RestaurantCategory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ItemRestoController {

    @FXML
    private Label CategoryLabel;

    @FXML
    private Label NameLabel;

    @FXML
    private ImageView img;

    private Restaurant restaurant;

    public void setData(Restaurant restaurant) {
        this.restaurant = restaurant;

        // Set the image, name, and category for the restaurant
        String imgSrc = restaurant.getImgSrc();

        if (imgSrc != null && !imgSrc.isEmpty()) {
            Image image = loadImage(imgSrc);
            if (image != null) {
                img.setImage(image);
            } else {
                // Handle the case where the image cannot be loaded
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

    private Image loadImage(String imgSrc) {
        try {
            // Convert to Path for better manipulation
            Path imagePath = Paths.get(imgSrc);

            // Check if the path starts with the database prefix
            String dbPrefix = "A:/xampp/htdocs/restaurant_images/";
            if (imgSrc.startsWith(dbPrefix)) {
                // Remove the database prefix
                imgSrc = imgSrc.substring(dbPrefix.length());
            }

            // Resolve the absolute path for the image using the provided base directory
            Path absolutePath = Paths.get("C:/xampp/htdocs/restaurant_images/").resolve(imagePath);

            System.out.println("Absolute Path: " + absolutePath);  // Debugging statement

            return new Image("file:" + absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }






}





