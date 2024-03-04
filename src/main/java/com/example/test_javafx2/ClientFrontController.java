package com.example.test_javafx2;

import entities.client;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.clientService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ClientFrontController implements Initializable {

    @FXML
    private ImageView backgroundcompteImageView;
    @FXML
    private ImageView maleImageView;
    @FXML
    private ImageView femaleImageView;
    @FXML
    private AnchorPane clientAncherPane;
    @FXML
    private Label echecAncienMdpLabel;
    @FXML
    private Label echecModifierAdresseLabel;
    @FXML
    private Label echecModifierEmailLabel;
    @FXML
    private Label echecModifierNomLabel;
    @FXML
    private Label echecModifierNumeroLabel;
    @FXML
    private Label echecModifierPrenomLabel;
    @FXML
    private Label echecNewMdpLabel;
    @FXML
    private Label echecmodifierLabel;
    @FXML
    private Label echecmodifiergenreLabel;
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
    private PasswordField ancienmdpPasswordField;
    @FXML
    private PasswordField newmdpPasswordField;
    @FXML
    private CheckBox femmeCheckBox;
    @FXML
    private CheckBox hommeCheckBox;
    @FXML
    private ImageView modifierclientImageView;
    @FXML
    private ImageView addImageView;
    @FXML
    private Circle cercle;
    @FXML
    private Button supprimerClientButton;
    @FXML
    private Button modifierClientButton;
    private clientService service;
    private String imagePath;

    // Handle checkbox selection
    // Méthode pour gérer la sélection du genre
    private void onGenreSelected(ActionEvent event) {
        CheckBox selectedCheckBox = (CheckBox) event.getSource();
        // Unselect the other checkbox
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
            imagePath = selectedFile.getPath(); // Utiliser getPath() pour récupérer le chemin d'accès du fichier
            Image image = new Image(new File(imagePath).toURI().toString());
            modifierclientImageView.setImage(image);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundcompteFile = new File("images/backgroundcompte.jpg");
        Image backgroundcompteImage = new Image(backgroundcompteFile.toURI().toString());
        backgroundcompteImageView.setImage(backgroundcompteImage);
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

        cercle.setOnMouseClicked(event -> selectImage(event));
        // Récupérer les informations du client à partir de l'e-mail de l'utilisateur connecté
        clientService service = new clientService();
        client clientInfo = service.getClientInfoByMail(UserSession.getInstance().getEmail());

        System.out.println("Client Info: " + clientInfo); // Ajoutez cette ligne pour vérifier les informations du client
        // Vérifier si les informations du client sont récupérées avec succès
        if (clientInfo != null) {
            // Définir le texte de chaque champ avec les informations récupérées
            modifiernomTextField.setText(clientInfo.getNom());
            modifierprenomTextField.setText(clientInfo.getPrenom());
            modifieremailTextField.setText(UserSession.getInstance().getEmail()); // Utilisez l'e-mail de l'utilisateur connecté
            modifiernumeroTextField.setText(clientInfo.getNumero());
            modifieradresseTextField.setText(clientInfo.getAdresse());
            // Cocher la case correspondant au genre du client
            if (clientInfo.getGenre() == client.GenreEnum.homme) {
                hommeCheckBox.setSelected(true);
            } else if (clientInfo.getGenre() == client.GenreEnum.femme) {
                femmeCheckBox.setSelected(true);
            }
            String imagePath = clientInfo.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image clientImage = new Image(imageFile.toURI().toString());
                    modifierclientImageView.setImage(clientImage);
                    addImageView.setImage(null);
                }}
            // Définir le champ de texte en lecture seule
            modifieremailTextField.setEditable(false);
        } else {
            System.out.println("Les informations du client ne sont pas trouvées.");
        }
        // Only allow digits and a maximum of 8 characters
        UnaryOperator<TextFormatter.Change> wholeNumberFilter = change -> {
            String newText = change.getControlNewText();
            //String textUsed = change.getText();
            //System.out.println("Total Change Control ->" + newText);
            //System.out.println("\nChange Control - **User Input ** ->" + textUsed + "\n");
            if (newText.matches("\\d*")) { // Vérifier si le texte entré ne contient que des chiffres
                echecModifierNumeroLabel.setText(""); // Effacer le message d'erreur
                modifiernumeroTextField.setStyle("-fx-background-color: white;"); // Réinitialiser la couleur de fond
                if (newText.length() > 0 && newText.length() < 8) {
                    modifiernumeroTextField.setStyle("-fx-background-color: pink;"); // Couleur rose si la longueur est entre 1 et 7
                }
                if (newText.length() == 8) {
                    modifiernumeroTextField.setStyle("-fx-background-color: green;"); // Couleur verte si la longueur est de 8
                }
                if (newText.length() <= 8) {
                    return change; // Autoriser le changement
                } else {
                    echecModifierNumeroLabel.setText("Max Input 8 numbers !!!"); // Afficher un message si la longueur dépasse 8
                }
            } else {
                echecModifierNumeroLabel.setText("Accepte seulement les chiffres"); // Afficher un message si un caractère autre que les chiffres est entré
            }
            return null; // Ne pas autoriser le changement si le texte entré n'est pas valide
        };
        TextFormatter<String> wholeNumberFormatter = new TextFormatter<>(wholeNumberFilter);
        modifiernumeroTextField.setTextFormatter(wholeNumberFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> nomFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*")) { // Seuls les caractères de l'alphabet sont autorisés
                echecModifierNomLabel.setText(""); // Efface le message d'erreur
                modifiernomTextField.setStyle("-fx-background-color: white;"); // Réinitialise la couleur de fond
                if (newText.length() > 0 && newText.length() < 3) {
                    modifiernomTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose si la longueur est entre 1 et 3
                }
                if (newText.length() > 2) {
                    modifiernomTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert si la longueur est de 8 caractères
                }
                return change;
            } else {
                echecModifierNomLabel.setText("Accepte seules les lettres de [a..z]"); // Affiche un message d'erreur si des caractères non autorisés sont entrés
            }
            return null;
        };
        TextFormatter<String> nomFormatter = new TextFormatter<>(nomFilter);
        modifiernomTextField.setTextFormatter(nomFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> prenomFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*")) { // Seuls les caractères de l'alphabet sont autorisés
                echecModifierPrenomLabel.setText(""); // Efface le message d'erreur
                modifierprenomTextField.setStyle("-fx-background-color: white;"); // Réinitialise la couleur de fond
                if (newText.length() > 0 && newText.length() < 3) {
                    modifierprenomTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose si la longueur est entre 1 et 3
                }
                if (newText.length() > 2) {
                    modifierprenomTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert si la longueur est de 8 caractères
                }
                return change;
            } else {
                echecModifierPrenomLabel.setText("Accepte seules les lettres de [a..z]"); // Affiche un message d'erreur si des caractères non autorisés sont entrés
            }
            return null;
        };
        TextFormatter<String> prenomFormatter = new TextFormatter<>(prenomFilter);
        modifierprenomTextField.setTextFormatter(prenomFormatter);
        //******************************************************************************************************
        // Contrôle de saisie pour l'adresse
        UnaryOperator<TextFormatter.Change> adresseFilter = change -> {
            String newText = change.getControlNewText();
            echecModifierAdresseLabel.setText(""); // Efface le message d'erreur
            if (newText.isEmpty()) { // Vérifier si le champ est vide
                modifieradresseTextField.setStyle("-fx-background-color: white;"); // Couleur de fond blanche
                return change; // Autoriser le changement
            } else if (newText.length() >= 3) { // Vérifier si la longueur de la saisie est d'au moins 3 caractères
                modifieradresseTextField.setStyle("-fx-background-color: green;"); // Change la couleur de fond en vert
            } else {
                modifieradresseTextField.setStyle("-fx-background-color: pink;"); // Change la couleur de fond en rose
            }
            return change;
        };
        TextFormatter<String> adresseFormatter = new TextFormatter<>(adresseFilter);
        modifieradresseTextField.setTextFormatter(adresseFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> mdpFilter = change -> {
            String newText = change.getControlNewText();
            echecAncienMdpLabel.setText(""); // Efface le message d'erreur
            if (newText.length() > 0 && newText.length() < 6) {
                ancienmdpPasswordField.setStyle("-fx-background-color: pink;");
            } else if (newText.length() > 5) {
                ancienmdpPasswordField.setStyle("-fx-background-color: green;");
            } else {
                ancienmdpPasswordField.setStyle("-fx-background-color: white;");
            }
            return change;
        };
        TextFormatter<String> mdpFormatter = new TextFormatter<>(mdpFilter);
        ancienmdpPasswordField.setTextFormatter(mdpFormatter);
        //******************************************************************************************************
        // Vérifier le format de l'email
        UnaryOperator<TextFormatter.Change> emailFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) { // Vérifier si le champ est vide
                modifieremailTextField.setStyle("-fx-background-color: white;"); // Couleur de fond blanche si le champ est vide
                return change; // Autoriser le changement
            } else if (newText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) { // Vérifier le format de l'e-mail
                echecModifierEmailLabel.setText(""); // Effacer le message d'erreur
                modifieremailTextField.setStyle("-fx-background-color: green;"); // Couleur de fond verte pour un format d'e-mail valide
                return change; // Autoriser le changement
            } else {
                echecModifierEmailLabel.setText("Format d'email invalide."); // Affiche un message d'erreur si le format de l'e-mail est invalide
                modifieremailTextField.setStyle("-fx-background-color: pink;"); // Couleur de fond rose pour indiquer une erreur
                return change; // Retourner le changement pour qu'il soit appliqué au champ
            }
        };
        TextFormatter<String> emailFormatter = new TextFormatter<>(emailFilter);
        modifieremailTextField.setTextFormatter(emailFormatter);
        //******************************************************************************************************
        UnaryOperator<TextFormatter.Change> newMdpFilter = change -> {
            String newText = change.getControlNewText();
            echecNewMdpLabel.setText(""); // Efface le message d'erreur
            if (newText.length() > 0 && newText.length() < 6) {
                newmdpPasswordField.setStyle("-fx-background-color: pink;");
            } else if (newText.length() > 5) {
                newmdpPasswordField.setStyle("-fx-background-color: green;");
            } else {
                newmdpPasswordField.setStyle("-fx-background-color: white;");
            }
            return change;
        };
        TextFormatter<String> newMdpFormatter = new TextFormatter<>(newMdpFilter);
        newmdpPasswordField.setTextFormatter(newMdpFormatter);
        //******************************************************************************************************
        // Set up event handlers for checkboxes
        hommeCheckBox.setOnAction(this::onGenreSelected);
        femmeCheckBox.setOnAction(this::onGenreSelected);
        addButtonHoverAnimation(modifierClientButton);
        addsuppAnimation(supprimerClientButton);
    }

    public void modifiererButtonOnAction(ActionEvent event) {
        // Récupérer les valeurs actuelles des champs à partir de l'objet clientInfo
        String nom = modifiernomTextField.getText();
        String prenom = modifierprenomTextField.getText();
        String email = modifieremailTextField.getText();
        String numero = modifiernumeroTextField.getText();
        String adresse = modifieradresseTextField.getText();
        String ancienPassword = ancienmdpPasswordField.getText();
        String newPassword = newmdpPasswordField.getText();
        // Récupérer le genre sélectionné
        client.GenreEnum genre = null;
        if (hommeCheckBox.isSelected()) {
            genre = client.GenreEnum.homme;
        } else if (femmeCheckBox.isSelected()) {
            genre = client.GenreEnum.femme;
        }

        // Vérifier si les champs non vides sont remplis
        if (!nom.isBlank() && !prenom.isBlank() && !numero.isBlank() && !adresse.isBlank()) {
            clientService service = new clientService();
            client clientInfo = service.getClientInfoByMail(email);
            if (clientInfo != null) {
                boolean updated = false; // Variable pour suivre si les informations du client ont été mises à jour

                // Vérifier si l'ancien mot de passe est saisi et s'il est correct
                if (!ancienPassword.isBlank() && service.validelogin(email, ancienPassword)) {
                    echecmodifierLabel.setText("");
                    // Si l'utilisateur souhaite modifier le numéro
                    if (!numero.equals(clientInfo.getNumero())) {
                        // Met à jour le numéro si le format est correct et s'il n'existe pas déjà
                        if (numero.length() == 8 && numero.matches("\\d{8}") && !service.numeroExists(numero)) {
                            clientInfo.setNumero(numero);
                            updated = true;
                        } else {
                            echecModifierNumeroLabel.setText("Veuillez vérifier le numéro.");
                            return; // Sort de la méthode si les conditions ne sont pas remplies
                        }
                    }
                    // Vérifier la longueur du nom
                    if (modifiernomTextField.getText().length() < 3) {
                        echecModifierNomLabel.setText("Le nom doit contenir au moins 3 caractères.");
                        return;
                    }

                    // Vérifier la longueur du prenom
                    if (modifierprenomTextField.getText().length() < 3) {
                        echecModifierPrenomLabel.setText("Le prénom doit contenir au moins 3 caractères.");
                        return;
                    }

                    // Vérifier la longueur de l'adresse
                    if (modifieradresseTextField.getText().length() < 3) {
                        echecModifierAdresseLabel.setText("L'adresse doit contenir au moins 3 caractères.");
                        return;
                    }

                    if (newPassword.isBlank()) {
                        clientInfo.setNom(nom);
                        clientInfo.setPrenom(prenom);
                        clientInfo.setNumero(numero);
                        clientInfo.setAdresse(adresse);
                        clientInfo.setNewpassword(clientService.encrypt(ancienPassword));
                        if (genre != null) {
                            clientInfo.setGenre(genre);
                        }
                        // Vérifier si une nouvelle image a été sélectionnée
                        if (imagePath != null && !imagePath.isEmpty()) {
                            clientInfo.setImage(imagePath);
                        }
                        updated = true;
                    }
                    // Si l'utilisateur souhaite modifier le mot de passe
                    if (!newPassword.isBlank()) {
                        // Vérifie si le nouveau mot de passe correspond à l'ancien mot de passe
                        if (newPassword.equals(ancienPassword)) {
                            echecNewMdpLabel.setText("Le nouveau mot de passe doit être différent de l'ancien.");
                            return; // Sort de la méthode si les conditions ne sont pas remplies
                        } else {
                            // Met à jour le mot de passe si le nouveau mot de passe a une longueur minimale
                            if (newPassword.length() >= 6) {
                                clientInfo.setNewpassword(clientService.encrypt(newPassword));
                                clientInfo.setNom(nom);
                                clientInfo.setPrenom(prenom);
                                clientInfo.setNumero(numero);
                                clientInfo.setAdresse(adresse);
                                if (genre != null) {
                                    clientInfo.setGenre(genre);
                                }
                                // Vérifier si une nouvelle image a été sélectionnée
                                if (imagePath != null && !imagePath.isEmpty()) {
                                    clientInfo.setImage(imagePath);
                                }
                                updated = true;
                            } else {
                                echecNewMdpLabel.setText("Le nouveau mot de passe doit avoir au moins 6 caractères.");
                                return; // Sort de la méthode si les conditions ne sont pas remplies
                            }
                        }
                    }

                    // Si les informations du client ont été mises à jour, effectue la mise à jour dans la base de données
                    if (updated) {
                        service.update(clientInfo);
                        echecmodifierLabel.setText("Les informations sont mises à jour avec succès !");
                    }
                    if (!updated) {
                        echecmodifierLabel.setText("Aucune mise à jour effectuée.");
                    }
                } else {
                    echecAncienMdpLabel.setText("Veuillez vérifier l'ancien mot de passe.");
                }
            } else {
                // Gère le cas où les informations du client ne sont pas trouvées
                System.out.println("Les informations du client ne sont pas trouvées.");
            }
        } else {
            echecmodifierLabel.setText("Veuillez remplir tous les champs.");
        }
    }
    public void supprimerButtonOnAction(ActionEvent event) {
        String email = modifieremailTextField.getText();
        String ancienPassword = ancienmdpPasswordField.getText();

        clientService service = new clientService();
        client clientInfo = service.getClientInfoByMail(email);

        if (clientInfo != null && service.validelogin(email, ancienPassword)) {
            // Supprimer le compte utilisateur
            service.delete(email);
            // Rediriger l'utilisateur vers la fenêtre de connexion
            redirectToLogin(event);
        } else {
            // Afficher un message d'erreur si l'email ou le mot de passe est incorrect
            // (ou si aucun compte correspondant n'est trouvé)
            echecAncienMdpLabel.setText("Email ou mot de passe incorrect.");
        }
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root, 520, 400));
            loginStage.setTitle("Wakkel Food");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void addsuppAnimation(Button button) {
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
}