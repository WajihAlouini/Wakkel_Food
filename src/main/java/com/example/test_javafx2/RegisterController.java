package com.example.test_javafx2;

import entities.client;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.clientService;
import org.controlsfx.control.Notifications;

/*import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class RegisterController implements Initializable {
    @FXML
    private Button randomkeysendButton;
    @FXML
    private Button retournerButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label echecregisterLabel;
    @FXML
    private Label echecregisternomLabel;
    @FXML
    private Label echecregisterprenomLabel;
    @FXML
    private Label echecregisteremailLabel;
    @FXML
    private Label echecregisternumeroLabel;
    @FXML
    private Label echecregisteradresseLabel;
    @FXML
    private Label echecregistermdpLabel;
    @FXML
    private Label echecregistergenreLabel;
    @FXML
    private Label echeccodeLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView backgroundinscriptionImageView;
    @FXML
    private ImageView maleImageView;
    @FXML
    private ImageView femaleImageView;
    @FXML
    private ImageView clientImageView;
    @FXML
    private ImageView cameraImageView;
    @FXML
    private ImageView addImageView;
    @FXML
    private TextField registernomTextField;
    @FXML
    private TextField registerprenomTextField;
    @FXML
    private TextField registeremailTextField;
    @FXML
    private TextField registernumeroTextField;
    @FXML
    private TextField registeradresseTextField;
    @FXML
    private PasswordField registermdpPasswordField;
    @FXML
    private TextField randomkeyTextField;
    @FXML
    private CheckBox hommeCheckBox;
    @FXML
    private CheckBox femmeCheckBox;
    @FXML
    private Circle cercle;
    private String imagePath;
    private clientService service;
    private String randomKeySentToUser;
    // Méthode pour générer une clé aléatoire
    private String generateRandomKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            key.append(characters.charAt(random.nextInt(characters.length())));
        }
        return key.toString();
    }
    // Méthode pour envoyer un e-mail avec la clé aléatoire
    private void sendRandomKeyByEmail(String email, String randomKey) {
        String from = "guesmiaymen181@gmail.com"; // Remplacez par votre adresse e-mail
        String host = "smtp.gmail.com"; // Remplacez par l'hôte SMTP
        String password = "rbga pqoz ozid lvxg"; // Remplacez par votre mot de passe SMTP

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Votre clé aléatoire pour la vérification");
            message.setText("Votre clé aléatoire est : " + randomKey);
            Transport.send(message);
            randomKeySentToUser = randomKey; // Stocker la clé aléatoire envoyée à l'utilisateur
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    // Handle checkbox selection
    private void onGenreSelected(ActionEvent event) {
        CheckBox selectedCheckBox = (CheckBox) event.getSource();

        // Unselect the other checkbox
        if (selectedCheckBox == hommeCheckBox) {
            femmeCheckBox.setSelected(false);
        } else {
            hommeCheckBox.setSelected(false);
        }
    }
    public void clearTextFields() {
        registernomTextField.clear();
        registerprenomTextField.clear();
        registeremailTextField.clear();
        registernumeroTextField.clear();
        registeradresseTextField.clear();
        registermdpPasswordField.clear();
        randomkeyTextField.clear();
        echeccodeLabel.setText("");
    }
    @FXML
    void selectImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getPath(); // Utiliser getPath() pour récupérer le chemin d'accès du fichier
            Image image = new Image(new File(imagePath).toURI().toString());
            clientImageView.setImage(image);
            addImageView.setImage(null);
        }
    }
    @Override
    public void initialize(URL url,ResourceBundle resourceBundle) {
        this.service = new clientService();
        File logoFile = new File("images/logo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
        File backgroundinscriptionFile = new File("images/backgroundinscription.png");
        Image backgroundinscriptionImage = new Image(backgroundinscriptionFile.toURI().toString());
        backgroundinscriptionImageView.setImage(backgroundinscriptionImage);
        this.service = new clientService();
        File maleFile = new File("images/male.png");
        Image maleImage = new Image(maleFile.toURI().toString());
        maleImageView.setImage(maleImage);
        this.service = new clientService();
        File femaleFile = new File("images/female.png");
        Image femaleImage = new Image(femaleFile.toURI().toString());
        femaleImageView.setImage(femaleImage);
        File addFile = new File("images/add.png");
        Image addImage = new Image(addFile.toURI().toString());
        addImageView.setImage(addImage);
        /*File cameraFile = new File("images/camera.png");
        Image cameraImage = new Image(cameraFile.toURI().toString());
        cameraImageView.setImage(cameraImage);
        cameraImageView.setOnMouseClicked(event -> selectImage(event));*/

        cercle.setOnMouseClicked(event -> selectImage(event));

        addButtonHoverAnimation(retournerButton);
        addButtonHoverAnimation(registerButton);
        addenvoyerAnimation(randomkeysendButton);

        // Set up event handlers for checkboxes
        hommeCheckBox.setOnAction(this::onGenreSelected);
        femmeCheckBox.setOnAction(this::onGenreSelected);

        registermdpPasswordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                registerButton.fire();
            }
        });

        // Only allow digits and a maximum of 8 characters
        UnaryOperator<TextFormatter.Change> wholeNumberFilter = change -> {
            String newText = change.getControlNewText();
            //String textUsed = change.getText();
            //System.out.println("Total Change Control ->" + newText);
            //System.out.println("\nChange Control - **User Input ** ->" + textUsed + "\n");
            if (newText.matches("\\d*")) { // Vérifier si le texte entré ne contient que des chiffres
                echecregisternumeroLabel.setText(""); // Effacer le message d'erreur
                registernumeroTextField.setStyle("-fx-background-color: white;"); // Réinitialiser la couleur de fond
                if (newText.length() > 0 && newText.length() < 8) {
                    registernumeroTextField.setStyle("-fx-background-color: pink;"); // Couleur rose si la longueur est entre 1 et 7
                }
                if (newText.length() == 8) {
                    registernumeroTextField.setStyle("-fx-background-color: green;"); // Couleur verte si la longueur est de 8
                }
                if (newText.length() <= 8) {
                    return change; // Autoriser le changement
                } else {
                    echecregisternumeroLabel.setText("Max Input 8 numbers !!!"); // Afficher un message si la longueur dépasse 8
                }
            } else {
                echecregisternumeroLabel.setText("Accepte seulement les chiffres"); // Afficher un message si un caractère autre que les chiffres est entré
            }
            return null; // Ne pas autoriser le changement si le texte entré n'est pas valide
        };
        TextFormatter<String> wholeNumberFormatter = new TextFormatter<>(wholeNumberFilter);
        registernumeroTextField.setTextFormatter(wholeNumberFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> nameFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*")) { // Seuls les caractères de l'alphabet sont autorisés
                echecregisternomLabel.setText(""); // Efface le message d'erreur
                registernomTextField.setStyle("-fx-background-color: white;"); // Réinitialise la couleur de fond
                if (newText.length() > 0 && newText.length() < 3) {
                    registernomTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose si la longueur est entre 1 et 3
                }
                if (newText.length() > 2) {
                    registernomTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert si la longueur est de 8 caractères
                }
                return change;
            } else {
                echecregisternomLabel.setText("Accepte seules les lettres de [a..z]"); // Affiche un message d'erreur si des caractères non autorisés sont entrés
            }
            return null;
        };
        TextFormatter<String> nameFormatter = new TextFormatter<>(nameFilter);
        registernomTextField.setTextFormatter(nameFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> prenomFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*")) { // Seuls les caractères de l'alphabet sont autorisés
                echecregisterprenomLabel.setText(""); // Efface le message d'erreur
                registerprenomTextField.setStyle("-fx-background-color: white;"); // Réinitialise la couleur de fond
                if (newText.length() > 0 && newText.length() < 3) {
                    registerprenomTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose si la longueur est entre 1 et 3
                }
                if (newText.length() > 2) {
                    registerprenomTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert si la longueur est de 8 caractères
                }
                return change;
            } else {
                echecregisterprenomLabel.setText("Accepte seules les lettres de [a..z]"); // Affiche un message d'erreur si des caractères non autorisés sont entrés
            }
            return null;
        };
        TextFormatter<String> prenomFormatter = new TextFormatter<>(prenomFilter);
        registerprenomTextField.setTextFormatter(prenomFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> mdpFilter = change -> {
            String newText = change.getControlNewText();
            echecregistermdpLabel.setText(""); // Efface le message d'erreur
            if (newText.length() > 0 && newText.length() < 6) {
                registermdpPasswordField.setStyle("-fx-background-color: pink;");
            } else if (newText.length() > 5) {
                registermdpPasswordField.setStyle("-fx-background-color: green;");
            } else {
                registermdpPasswordField.setStyle("-fx-background-color: white;");
            }
            return change;
        };
        TextFormatter<String> mdpFormatter = new TextFormatter<>(mdpFilter);
        registermdpPasswordField.setTextFormatter(mdpFormatter);
        //******************************************************************************************************
        // Vérifier le format de l'email
        UnaryOperator<TextFormatter.Change> emailFilter = change -> {
            String newText = change.getControlNewText();
            echecregisteremailLabel.setText("");
            if (newText.isEmpty()) { // Vérifier si le champ est vide
                registeremailTextField.setStyle("-fx-background-color: white;"); // Couleur de fond blanche si le champ est vide
                return change; // Autoriser le changement
            } else if (newText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) { // Vérifier le format de l'e-mail
                echecregisteremailLabel.setText(""); // Effacer le message d'erreur
                registeremailTextField.setStyle("-fx-background-color: green;"); // Couleur de fond verte pour un format d'e-mail valide
                return change; // Autoriser le changement
            } else {
                registeremailTextField.setStyle("-fx-background-color: pink;"); // Couleur de fond rose pour indiquer une erreur
                return change; // Retourner le changement pour qu'il soit appliqué au champ
            }
        };
        TextFormatter<String> emailFormatter = new TextFormatter<>(emailFilter);
        registeremailTextField.setTextFormatter(emailFormatter);
        //******************************************************************************************************
        // Contrôle de saisie pour l'adresse
        UnaryOperator<TextFormatter.Change> adresseFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) { // Vérifier si le champ est vide
                registeradresseTextField.setStyle("-fx-background-color: white;"); // Couleur de fond blanche
                return change; // Autoriser le changement
            } else if (newText.length() >= 3) { // Vérifier si la longueur de la saisie est d'au moins 3 caractères
                echecregisteradresseLabel.setText(""); // Efface le message d'erreur
                registeradresseTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert
            } else {
                registeradresseTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose
            }
            return change;
        };
        TextFormatter<String> adresseFormatter = new TextFormatter<>(adresseFilter);
        registeradresseTextField.setTextFormatter(adresseFormatter);
        //******************************************************************************************************
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
    private void addenvoyerAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #A32828;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #ff0000;");
        });
    }
        public void registerButtonOnAction(ActionEvent event) {
            String nom = registernomTextField.getText();
            String prenom = registerprenomTextField.getText();
            String email = registeremailTextField.getText();
            String numero = registernumeroTextField.getText();
            String adresse = registeradresseTextField.getText();
            String password = registermdpPasswordField.getText();
            String enteredRandomKey = randomkeyTextField.getText();
            String encryptedPassword = clientService.encrypt(password);


            // Vérifier les champs non vides
            if (!nom.isBlank() && !prenom.isBlank() && !email.isBlank() &&
                    !numero.isBlank() && !adresse.isBlank() && !password.isBlank()) {
                // Vérifier si le numéro contient exactement 8 chiffres
                if (numero.length() != 8 || !numero.matches("\\d{8}")) {
                    echecregisternumeroLabel.setText("Le numéro doit contenir 8 chiffres.");
                    return; // Sortie de la méthode si la condition n'est pas remplie
                }

                // Vérifier si le mot de passe contient au moins 6 caractères
                if (password.length() < 6) {
                    echecregistermdpLabel.setText("Le mdp doit contenir au moins 6 caractères.");
                    return; // Sortie de la méthode si la condition n'est pas remplie
                }

                // Vérifier le format de l'email
                if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    echecregisteremailLabel.setText("Format d'email invalide.");
                    return; // Sortie de la méthode si la condition n'est pas remplie
                }

                // Vérifier si l'email existe déjà dans la base de données
                if (service.emailExists(email)) {
                    echecregisteremailLabel.setText("L'email est déjà utilisé.");
                    return; // Sortie de la méthode si la condition n'est pas remplie
                }

                // Vérifier si le numéro existe déjà dans la base de données
                if (service.numeroExists(numero)) {
                    echecregisternumeroLabel.setText("Le numéro est déjà utilisé.");
                    return; // Sortie de la méthode si la condition n'est pas remplie
                }

                // Vérifier la longueur du nom
                if (registernomTextField.getText().length() < 3) {
                    echecregisternomLabel.setText("Le nom doit contenir au moins 3 caractères.");
                    return;
                }

                // Vérifier la longueur du prénom
                if (registerprenomTextField.getText().length() < 3) {
                    echecregisterprenomLabel.setText("Le prénom doit contenir au moins 3 caractères.");
                    return;
                }

                // Vérifier la longueur de l'adresse
                if (registeradresseTextField.getText().length() < 3) {
                    echecregisteradresseLabel.setText("L'adresse doit contenir au moins 3 caractères.");
                    return;
                }

                // Vérifiez le genre sélectionné
                client.GenreEnum genre = null;
                if (hommeCheckBox.isSelected()) {
                    genre = client.GenreEnum.homme;
                } else if (femmeCheckBox.isSelected()) {
                    genre = client.GenreEnum.femme;
                }

                    // Vérifiez si un genre a été sélectionné
                    if (genre == null) {
                        // Afficher un message d'erreur
                        echecregistergenreLabel.setText("Veuillez sélectionner un genre.");
                        return;
                    }
                    String imagePath = this.imagePath;

                // Vérifier si la clé saisie correspond à celle envoyée à l'utilisateur lors de l'inscription
                if (!enteredRandomKey.equals(randomKeySentToUser)) {
                    echeccodeLabel.setText("Veuillez entrer le clode reçu.");
                    return;
                }
                    // Si toutes les conditions sont remplies, ajoutez le client
                    service.add(nom, prenom, email, numero, adresse, encryptedPassword, genre, imagePath);
                    echecregisterLabel.setText("Votre compte a été créé avec succès !");
                // Création de la notification
                Notifications notification = Notifications.create()
                        .title("Succès")
                        .text("Votre compte a été créé avec succès !")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT) // Position à droite de l'écran
                        .graphic(new ImageView(new File("images/tic.png").toURI().toString()));

                // Affichage de la notification
                  notification.show();
                    clearTextFields(); // Efface les champs de texte
                } else {
                    echecregisterLabel.setText("Entrez vos données.");
                }
            }

    public void retournerButtonOnAction(ActionEvent event){
        try{
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
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
    @FXML
    public void onEnterKeyPressed() {
        // Listener for Enter key pressed in the password field
        registerButton.fire();
    }
    @FXML
    void sendRandomKeyButtonOnAction(ActionEvent event) {
        String email = registeremailTextField.getText();
        if (!email.isEmpty()) {
            // Générer une clé aléatoire
            String randomKey = generateRandomKey();
            // Envoyer la clé aléatoire à l'utilisateur par e-mail
            sendRandomKeyByEmail(email, randomKey);
            // Afficher un message de succès ou une notification
            Notifications.create()
                    .title("Clé aléatoire envoyée")
                    .text("La clé aléatoire a été envoyée à votre adresse e-mail.")
                    .showInformation();
        } else {
            // Afficher un message d'erreur si l'email est vide
            // (vous pouvez également utiliser une alerte)
            System.out.println("Veuillez entrer votre adresse e-mail.");
        }
    }
}