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

public class MenuFrontController implements Initializable {
    @FXML
    private Label nomloginLabel;
    @FXML
    private Label prenomloginLabel;
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
    private Button evaluationButton;
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
    private ImageView clientmenuImageView;
    @FXML
    private ImageView refreshImageView;
    @FXML
    private ImageView evaluationImageView;
    @FXML
    private ImageView listevImageView;
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
        File logoutFile = new File("images/logout.png");
        Image logoutImage = new Image(logoutFile.toURI().toString());
        logoutImageView.setImage(logoutImage);
        File refreshFile = new File("images/refresh.png");
        Image refreshImage = new Image(refreshFile.toURI().toString());
        refreshImageView.setImage(refreshImage);
        File evaluationFile = new File("images/reclamation.png");
        Image evaluationImage = new Image(evaluationFile.toURI().toString());
        evaluationImageView.setImage(evaluationImage);
        File listevFile = new File("images/listev.png");
        Image listevImage = new Image(listevFile.toURI().toString());
        listevImageView.setImage(listevImage);

        // Ajout d'un effet CSS et gestionnaire d'événements pour refresh ImageView
        addHoverAnimation(refreshImageView);
        refreshImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            refreshClientImage();
        });
        addHoverAnimation(listevImageView);
        listevImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            listev();
        });

        // Retrieve the email of the logged-in user from the UserSession
        String userEmail = UserSession.getInstance().getEmail();

        // Use the email to fetch user information from the database
        clientService clientService = new clientService();
        client loggedInUser = clientService.getClientInfoByMail(userEmail);

        // Set the name and surname of the logged-in user in the labels
        nomloginLabel.setText(loggedInUser.getNom());
        prenomloginLabel.setText(loggedInUser.getPrenom());

        // Load the client's image
        refreshClientImage();

        // Add event handlers for button hover animations
        addHoverAnimation(compteButton);
        addHoverAnimation(restaurantButton);
        addHoverAnimation(evenementButton);
        addHoverAnimation(commandeButton);
        addHoverAnimation(reclamationButton);
        addHoverAnimation(evaluationButton);
        addLogoutButtonHoverAnimation(logoutButton);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frontResto.fxml"));
            AnchorPane clientFront = loader.load();

            // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
            emptyAnchorPane.getChildren().add(clientFront);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    // Method to refresh the client's image
    private void refreshClientImage() {
        // Retrieve the email of the logged-in user from the UserSession
        String userEmail = UserSession.getInstance().getEmail();

        // Use the email to fetch user information from the database
        clientService clientService = new clientService();
        client loggedInUser = clientService.getClientInfoByMail(userEmail);

        // Retrieve the image path from the database
        String imagePath = loggedInUser.getImage();
        client.GenreEnum genre = loggedInUser.getGenre();

        // Default image paths for male and female
        String defaultImageclientPath = "images/client.png";
        String defaultImageclientePath = "images/cliente.png";

        // Check if the image path retrieved from the database is null or empty
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image clientImage = new Image(imageFile.toURI().toString());
                clientmenuImageView.setImage(clientImage);
            } else {
                // Image file does not exist
                handleDefaultImage(genre, defaultImageclientPath, defaultImageclientePath);
            }
        } else {
            // Image path is null or empty
            handleDefaultImage(genre, defaultImageclientPath, defaultImageclientePath);
        }
    }

    // Handle setting default image
    private void handleDefaultImage(client.GenreEnum genre, String defaultImageclientPath, String defaultImageclientePath) {
        Image defaultImage;
        if (genre == client.GenreEnum.homme) {
            defaultImage = new Image(new File(defaultImageclientPath).toURI().toString());
        } else if (genre == client.GenreEnum.femme) {
            defaultImage = new Image(new File(defaultImageclientePath).toURI().toString());
        } else {
            defaultImage = new Image(new File(defaultImageclientPath).toURI().toString());
        }
        clientmenuImageView.setImage(defaultImage);
    }

    // Method to add hover animation to buttons
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

    // Method to add hover animation to logout button
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

    // Method to handle button actions
    public void ButtonsMenuOnAction(ActionEvent event) {
        if (event.getSource() == compteButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("clientFront.fxml"));
                AnchorPane clientFront = loader.load();

                // Add the clientFront.fxml interface to the AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == restaurantButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("frontResto.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == evenementButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("front.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == reclamationButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterReclamation.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == evaluationButton) {
            emptyAnchorPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterEvaluation.fxml"));
                AnchorPane clientFront = loader.load();

                // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
                emptyAnchorPane.getChildren().add(clientFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void listev(){
    emptyAnchorPane.getChildren().clear();
            try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ListeEvaluationFront.fxml"));
        AnchorPane clientFront = loader.load();

        // Ajouter l'interface de clientFront.fxml à l'AnchorPane clientAncherPane
        emptyAnchorPane.getChildren().add(clientFront);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
