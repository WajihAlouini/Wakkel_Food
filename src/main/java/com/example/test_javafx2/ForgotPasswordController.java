package com.example.test_javafx2;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import service.clientService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private AnchorPane forgotAnchorPane;
    @FXML
    private ImageView sloganImageView;
    @FXML
    private Label retournerforgetmdpLabel;
    @FXML
    private Label echecforgotLabel;
    @FXML
    private ImageView backgroundfoodImageView;
    @FXML
    private ImageView lockIlmageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private TextField forgotemailTextField;
    @FXML
    private Button forgotButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File sloganFile = new File("images/slogan.jpg");
        Image sloganImage = new Image(sloganFile.toURI().toString());
        sloganImageView.setImage(sloganImage);
        File lockFile = new File("images/lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockIlmageView.setImage(lockImage);
        File userFile = new File("images/user.png");
        Image userImage = new Image(userFile.toURI().toString());
        userImageView.setImage(userImage);
        File backgroundfoodFile = new File("images/backgroundfood.png");
        Image backgroundfoodImage = new Image(backgroundfoodFile.toURI().toString());
        backgroundfoodImageView.setImage(backgroundfoodImage);
        retournerforgetmdpLabel.setOnMouseClicked(event -> {
            try {
                loadThird(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addButtonHoverAnimation(forgotButton);
        addLabelHoverAnimation(retournerforgetmdpLabel);
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

    @FXML
    private void handleForgotEmail() {
        String email = forgotemailTextField.getText().trim();

        if (email.isEmpty()) {
            echecforgotLabel.setText("Donnez votre email");
        } else {
            clientService service = new clientService();
            if (!service.emailExists(email)) {
                echecforgotLabel.setText("Email n'existe pas");
            } else {
                // Récupérer le mot de passe associé à cet email depuis la base de données
                String password = service.getPasswordByEmail(email);

                // Envoyer le mot de passe à l'utilisateur
                sendEmail(email, password);
                echecforgotLabel.setText("Mot de passe envoyé");
                Notifications notification = Notifications.create()
                        .title("Mot de passe envoyée")
                        .text("Votre mot de passe a été envoyée à votre adresse e-mail.")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT) // Position à droite de l'écran
                        .graphic(new ImageView(new File("images/tic.png").toURI().toString()));
                notification.show();
            }
        }
    }
    private void sendEmail(String email, String password) {
        String from = "guesmiaymen181@gmail.com";
        String host = "smtp.gmail.com";
        String passwordSMTP = "rbga pqoz ozid lvxg";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, passwordSMTP);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Récupération de mot de passe");
            message.setText("Votre mot de passe est : " + password);
            Transport.send(message);
            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
    private void loadThird(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = retournerforgetmdpLabel.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) retournerforgetmdpLabel.getScene().getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(forgotAnchorPane);
        });
        timeline.play();
    }
}
