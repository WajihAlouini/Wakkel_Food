package com.example.test_javafx2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class banController  implements Initializable {
    @FXML
    private ImageView banImageView;
    @FXML
    private ImageView chaineImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File banFile = new File("images/ban.png");
        Image banImage = new Image(banFile.toURI().toString());
        banImageView.setImage(banImage);
        File chaineFile = new File("images/chaine.png");
        Image chaineImage = new Image(chaineFile.toURI().toString());
        chaineImageView.setImage(chaineImage);
    }
}
