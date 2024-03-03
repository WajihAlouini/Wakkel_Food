package com.example.test_javafx2;

import entities.client;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import service.clientService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import entities.client;

public class AdminClientController implements Initializable {

    @FXML
    private ImageView backgroundcompteImageView;
    @FXML
    private Label echecmodifierLabel;
    @FXML
    private TextField modifieradresseTextField;
    @FXML
    private TextField modifieremailTextField;
    @FXML
    private TextField modifiernomTextField;
    @FXML
    private TextField modifiernumeroTextField;
    @FXML
    private TextField modifierprenomTextField;
    @FXML
    private TextField modifiermdpTextField;
    @FXML
    private CheckBox femmeCheckBox;
    @FXML
    private CheckBox hommeCheckBox;
    @FXML
    private ImageView modifierclientImageView;
    @FXML
    private ImageView trashImageView;
    @FXML
    private ImageView addImageView;
    @FXML
    private Circle cercle;
    @FXML
    private Button modifierClientButton;
    private clientService clientService;
    private String imagePath;
    private String oldImagePath;

    public void clearImage(MouseEvent event) {
        File addFile = new File("images/add.png");
        Image addImage = new Image(addFile.toURI().toString());
        addImageView.setImage(addImage);
        modifierclientImageView.setImage(null);
        imagePath = null;
        oldImagePath=null;
    }
    private void onGenreSelected(ActionEvent event) {
        CheckBox selectedCheckBox = (CheckBox) event.getSource();
        if (selectedCheckBox == hommeCheckBox) {
            femmeCheckBox.setSelected(false);
        } else {
            hommeCheckBox.setSelected(false);
        }
    }
    @FXML
    void selectImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getPath();
            Image image = new Image(new File(imagePath).toURI().toString());
            modifierclientImageView.setImage(image);
            addImageView.setImage(null);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundcompteFile = new File("images/backgroundcompte.jpg");
        Image backgroundcompteImage = new Image(backgroundcompteFile.toURI().toString());
        backgroundcompteImageView.setImage(backgroundcompteImage);
        File trashFile = new File("images/trash.png");
        Image trashImage = new Image(trashFile.toURI().toString());
        trashImageView.setImage(trashImage);
        File addFile = new File("images/add.png");
        Image addImage = new Image(addFile.toURI().toString());
        addImageView.setImage(addImage);

        clientService = new clientService();
        hommeCheckBox.setOnAction(this::onGenreSelected);
        femmeCheckBox.setOnAction(this::onGenreSelected);
        cercle.setOnMouseClicked(event -> selectImage(event));
        trashImageView.setOnMouseClicked(event -> clearImage(event));
        addHoverAnimation(trashImageView);
        addButtonHoverAnimation(modifierClientButton);
    }
    private void addHoverAnimation(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(201,201,201,0.8), 10, 0, 0, 0);");
        });
        imageView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            imageView.setScaleX(1);
            imageView.setScaleY(1);
            imageView.setStyle("");
        });
    }
    private void addButtonHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #1449B3;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #2980b9;");
        });
    }
    public void initData(client client) {
        modifiernomTextField.setText(client.getNom());
        modifierprenomTextField.setText(client.getPrenom());
        modifieremailTextField.setText(client.getEmail());
        modifiernumeroTextField.setText(client.getNumero());
        modifieradresseTextField.setText(client.getAdresse());
        modifiermdpTextField.setText(clientService.decrypt(client.getNewpassword()));
        if (client.getGenre() == entities.client.GenreEnum.homme) {
            hommeCheckBox.setSelected(true);
        } else if (client.getGenre() == entities.client.GenreEnum.femme) {
            femmeCheckBox.setSelected(true);
        }
        String imagePath = client.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image clientImage = new Image(imageFile.toURI().toString());
                modifierclientImageView.setImage(clientImage);
                addImageView.setImage(null);
            }
        }
        oldImagePath = client.getImage();
    }

    public void modifiererButtonOnAction(ActionEvent event) {
        String nom = modifiernomTextField.getText();
        String prenom = modifierprenomTextField.getText();
        String email = modifieremailTextField.getText();
        String numero = modifiernumeroTextField.getText();
        String adresse = modifieradresseTextField.getText();
        String password = modifiermdpTextField.getText();
        client.GenreEnum genre = null;
        if (hommeCheckBox.isSelected()) {
            genre = client.GenreEnum.homme;
        } else if (femmeCheckBox.isSelected()) {
            genre = client.GenreEnum.femme;
        }

        if (!nom.isBlank() && !prenom.isBlank() && !numero.isBlank() && !email.isBlank() && !adresse.isBlank() && !password.isBlank()) {
            String encryptedPassword = clientService.encrypt(password);
            if (imagePath == null && oldImagePath != null) {
                // Si l'utilisateur n'a pas changé d'image, conservez l'ancienne image
                imagePath = oldImagePath;
            } else if (imagePath == null) {
                imagePath = null;
            }

            client clientToUpdate = new client(nom, prenom, email, numero, adresse, encryptedPassword, genre, imagePath);
            clientService.update(clientToUpdate);
            echecmodifierLabel.setText("Les informations sont mises à jour avec succès !");
        } else {
            echecmodifierLabel.setText("Veuillez remplir tous les champs.");
        }
    }
}
