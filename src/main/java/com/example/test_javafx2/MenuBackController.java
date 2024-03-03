package com.example.test_javafx2;

import entities.client;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.clientService;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;



public class MenuBackController implements Initializable {
    @FXML
    private Button compteButton;
    @FXML
    private Button restaurantButton;
    @FXML
    private Button evenementButton;
    @FXML
    private Button commandeButton;
    @FXML
    private Button reclamationButton;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView menuImageView;
    @FXML
    private ImageView compteImageView;
    @FXML
    private ImageView restaurantImageView;
    @FXML
    private ImageView evenementImageView;
    @FXML
    private ImageView commandeImageView;
    @FXML
    private ImageView reclamationImageView;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private ImageView adminImageView;
    @FXML
    private ImageView statistiqueImageView;
    @FXML
    private AnchorPane emptyAnchorPane;


    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        File menuFile = new File("images/menu.png");
        Image menuImage = new Image(menuFile.toURI().toString());
        menuImageView.setImage(menuImage);
        File compteFile = new File("images/compte.png");
        Image compteImage = new Image(compteFile.toURI().toString());
        compteImageView.setImage(compteImage);
        File restaurantFile = new File("images/restaurant.png");
        Image restaurantImage = new Image(restaurantFile.toURI().toString());
        restaurantImageView.setImage(restaurantImage);
        File evenementFile = new File("images/evenement.png");
        Image evenementImage = new Image(evenementFile.toURI().toString());
        evenementImageView.setImage(evenementImage);
        File commandeFile = new File("images/commande.png");
        Image commandeImage = new Image(commandeFile.toURI().toString());
        commandeImageView.setImage(commandeImage);
        File reclamationFile = new File("images/reclamation.png");
        Image reclamationImage = new Image(reclamationFile.toURI().toString());
        reclamationImageView.setImage(reclamationImage);
        File clientFile = new File("images/admin.png");
        Image clientImage = new Image(clientFile.toURI().toString());
        adminImageView.setImage(clientImage);
        File logoutFile = new File("images/logout.png");
        Image logoutImage = new Image(logoutFile.toURI().toString());
        logoutImageView.setImage(logoutImage);
        File statistiqueFile = new File("images/statistique.png");
        Image statistiqueImage = new Image(statistiqueFile.toURI().toString());
        statistiqueImageView.setImage(statistiqueImage);

        // Ajout d'un effet CSS et gestionnaire d'événements pour statistiqueImageView
        addHoverAnimation(statistiqueImageView);

        statistiqueImageView.setOnMouseClicked(this::openStatistiqueWindow);
        // Retrieve the email of the logged-in user from the UserSession
        String userEmail = UserSession.getInstance().getEmail();

        // Use the email to fetch user information from the database
        clientService clientService = new clientService();
        client loggedInUser = clientService.getClientInfoByMail(userEmail);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clientBack.fxml"));
            AnchorPane clientFront = loader.load();

            // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
            emptyAnchorPane.getChildren().add(clientFront);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Ajouter des gestionnaires d'événements pour la survol de la souris
        addHoverAnimation(compteButton);
        addHoverAnimation(restaurantButton);
        addHoverAnimation(evenementButton);
        addHoverAnimation(commandeButton);
        addHoverAnimation(reclamationButton);
        addLogoutButtonHoverAnimation(logoutButton);
    }


    // Méthode pour charger les images
    /*private void loadImages() {
        loadImage(menuImageView, "images/menu.png");
        loadImage(compteImageView, "images/compte.png");
        loadImage(restaurantImageView, "images/restaurant.png");
        loadImage(evenementImageView, "images/evenement.png");
        loadImage(commandeImageView, "images/commande.png");
        loadImage(reclamationImageView, "images/reclamation.png");
        loadImage(adminImageView, "images/admin.png");
        loadImage(logoutImageView, "images/logout.png");
        loadImage(statistiqueImageView, "images/statistique.png");
    }*/

    // Méthode utilitaire pour charger une image
    private void loadImage(ImageView imageView, String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView.setImage(image);
    }
    private void addHoverAnimation(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        });
        imageView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            imageView.setScaleX(1);
            imageView.setScaleY(1);
            imageView.setStyle("");
        });
    }

    private void openStatistiqueWindow(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("statistique.fxml"));
            AnchorPane statistiquePane = loader.load();

            // Ajouter le contenu de statistique.fxml à emptyAnchorPane
            emptyAnchorPane.getChildren().setAll(statistiquePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #2980b9;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #102b34;");
        });
    }
    private void addLogoutButtonHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #2980b9;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #1449B3;");
        });
    }
    public void ButtonsMenuOnAction(ActionEvent event) {
        if (event.getSource() == compteButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("clientBack.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (event.getSource() == restaurantButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (event.getSource() == evenementButton) {
            // Action pour le bouton d'événement
        } else if (event.getSource() == commandeButton) {
            // Action pour le bouton de commande
        } else if (event.getSource() == reclamationButton) {
            // Action pour le bouton de réclamation
        }
        if (event.getSource() == logoutButton) {
            // Détruire l'instance de la session utilisateur
            UserSession.destroyInstance();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            try{
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage registerstage= new Stage();
                registerstage.setScene(new Scene(root, 520, 400));
                registerstage.setTitle("Wakkel Food");
                registerstage.show();
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
}
