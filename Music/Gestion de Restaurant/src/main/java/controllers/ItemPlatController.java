package controllers;

import entities.Plat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ItemPlatController {
    @FXML
    private Label PlatIngredientLabel;

    @FXML
    private Label PlatNameLabel;

    @FXML
    private Label PlatPrixLabel;

    @FXML
    private ImageView pimg;

    public void setData(Plat plat) {
        // Set the image, name, and category for the plat
        String imgSrc = plat.getPimgSrc();

        if (imgSrc != null && !imgSrc.isEmpty()) {
            try {
                File imageFile = new File(imgSrc);

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    pimg.setImage(image);
                } else {
                    // Handle the case where the image is not found
                    System.err.println("Image not found: " + imgSrc);
                    // You can set a default image or take appropriate action.
                }
            } catch (Exception e) {
                // Handle any exception that might occur during image loading
                System.err.println("Error loading image for plat: " + plat.getNom_plat());
                e.printStackTrace(); // Print the stack trace for debugging
                // You can set a default image or take appropriate action.
            }
        } else {
            // Handle the case where imgSrc is null or empty
            // You can set a default image or take appropriate action.
            System.err.println("Image source is null or empty for plat: " + plat.getNom_plat());
        }

        PlatNameLabel.setText(plat.getNom_plat());
        PlatPrixLabel.setText("Prix(TND): " + plat.getPrix());
        PlatIngredientLabel.setText(plat.getIngredient());
    }
}
