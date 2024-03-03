package com.example.test_javafx2;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.clientService;


public class LoginController implements Initializable {
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private Label mdpoublierLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button loginregisterButton;
    @FXML
    private Label echecloginLabel;
    @FXML
    private ImageView sloganImageView;
    @FXML
    private ImageView lockIlmageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView cleImageView;
    @FXML
    private ImageView backgroundfoodImageView;
    @FXML
    private TextField loginemailTextField;
    @FXML
    private PasswordField loginmdpPasswordField;
    private clientService service; // Déclarez une instance

    private void clearTextFields(Parent root) {
        if (root == null) {
            return;
        }
        root.lookupAll(".text-field").forEach(node -> ((TextField) node).clear());
    }

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        this.service = new clientService();

            // Initialize method called by FXMLLoader
            // Set up event listener for Enter key press
            loginmdpPasswordField.setOnKeyPressed(event -> {
                if (event.getCode().toString().equals("ENTER")) {
                    loginButton.fire();
                }
            });

        File sloganFile = new File("images/slogan.jpg");
        Image sloganImage = new Image(sloganFile.toURI().toString());
        sloganImageView.setImage(sloganImage);
        File lockFile = new File("images/lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockIlmageView.setImage(lockImage);
        File userFile = new File("images/user.png");
        Image userImage = new Image(userFile.toURI().toString());
        userImageView.setImage(userImage);
        File cleFile = new File("images/cle.png");
        Image cleImage = new Image(cleFile.toURI().toString());
        cleImageView.setImage(cleImage);
        File backgroundfoodFile = new File("images/backgroundfood.png");
        Image backgroundfoodImage = new Image(backgroundfoodFile.toURI().toString());
        backgroundfoodImageView.setImage(backgroundfoodImage);
        mdpoublierLabel.setOnMouseClicked(event -> {
            try {
                loadThird(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addButtonHoverAnimation(loginregisterButton);
        addButtonHoverAnimation(loginButton);
        addLabelHoverAnimation(mdpoublierLabel);
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
    private void addLabelHoverAnimation(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            label.setStyle("-fx-background-color: #8D8D8D;");
        });
        label.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            label.setScaleX(1);
            label.setScaleY(1);
            label.setStyle("-fx-background-color: #112327;");
        });
    }
    public void loginButtonOnAction(ActionEvent event)throws IOException {
        String email = loginemailTextField.getText();
        String password = loginmdpPasswordField.getText();
        boolean banne = service.isClientBanned(email);
        // Vérifier les champs non vides
        if (!email.isBlank() && !password.isBlank()) {
            // Vérifier la connexion avec clientService
            boolean loginSuccessful = service.validelogin(email, password);
            if (loginSuccessful) {
                if (loginSuccessful) {
                    if (banne==false){
                    // Récupérer le rôle de l'utilisateur à partir du service de connexion
                    String role = service.getRole(email);

                    // Stocker l'e-mail et le rôle de l'utilisateur dans la session utilisateur
                    UserSession session = UserSession.getInstance();
                    session.setEmail(email);
                    session.setRole(role);

                    // Rediriger l'utilisateur en fonction de son rôle
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                    try {
                        String menuFXML = role.equals("admin") ? "menuBack.fxml" : "menuFront.fxml";
                        Parent root = FXMLLoader.load(getClass().getResource(menuFXML));
                        Stage menuStage = new Stage();
                        menuStage.setScene(new Scene(root, 1280, 700));
                        menuStage.setTitle("Wakkel Food");
                        menuStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        currentStage.close();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("ban.fxml"));
                            Stage menuStage = new Stage();
                            menuStage.setScene(new Scene(root, 1280, 700));
                            menuStage.setTitle("Wakkel Food");
                            menuStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // Authentification échouée
                echecloginLabel.setText("Connexion invalide. Veuillez réessayer.");
                clearTextFields(loginemailTextField.getParent());
            }

            } else {
            echecloginLabel.setText("Entrer votre email et mot de passe");
        }
    }

    public void registerloginButtonOnAction(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage registerstage= new Stage();
            registerstage.setScene(new Scene(root, 763, 587));
            registerstage.setTitle("Wakkel Food");
            registerstage.show();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    public void onEnterKeyPressed() {
        // Listener for Enter key pressed in the password field
        loginButton.fire();
    }
    private void loadThird(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("forgotPass.fxml"));
        Scene scene = mdpoublierLabel.getScene();
        root.translateXProperty().set(-scene.getWidth()); // Position initiale à l'extérieur de la scène du côté opposé

        StackPane parentContainer = (StackPane) mdpoublierLabel.getScene().getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN); // Glisser vers le centre de la scène
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(loginAnchorPane);
        });
        timeline.play();
    }
}
