package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import gestion_reclamation.entities.reclamation;
import gestion_reclamation.service.reclamationservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AjouterReclamation {

    @FXML
    private TextArea descriptionfield;

    @FXML
    private ChoiceBox<String> typefield;

    @FXML
    private TextField captchaField;

    @FXML
    private TextField userCaptchaInput;

    @FXML
    private Button uploadButton;

    private String generatedCaptcha;
    private InputStream uploadedImageInputStream;
    private List<String> badWordsList;

    public AjouterReclamation() {
        badWordsList = new ArrayList<>();
        badWordsList.add("badword1");
        badWordsList.add("badword2");
        // Add more bad words as needed
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            System.out.println("Selected Image: " + imagePath);

            try {
                uploadedImageInputStream = new FileInputStream(selectedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Image uploaded successfully!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Aucune image sélectionnée.");
            alert.show();
        }
    }

    @FXML
    void ajouterReclamation(ActionEvent event) {
        String description = descriptionfield.getText();
        String selectedType = typefield.getValue();
        String enteredCaptcha = userCaptchaInput.getText();

        if (containsBadWords(description)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("La description contient des mots inappropriés.");
            alert.show();
            return;
        }

        if (selectedType != null && !selectedType.isEmpty()) {
            if (description != null && !description.trim().isEmpty()) {
                if (enteredCaptcha.equals(generatedCaptcha)) {
                    reclamationservice reclamationservicex = new reclamationservice();
                    reclamation reclamationx = new reclamation(0, 0, null, selectedType, description, null);
                    reclamationservicex.add(reclamationx, uploadedImageInputStream);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Réclamation ajoutée avec succès");
                    alert.show();

                    generatedCaptcha = generateCaptcha();
                    captchaField.setText(generatedCaptcha);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Le captcha saisi est incorrect.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Le champ description ne peut pas être vide.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le champ type ne peut pas être vide.");
            alert.show();
        }
    }

    @FXML
    void initialize() {
        typefield.getItems().addAll("Retard", "Articles Manquants", "Commande Incorrecte", "Problème Qualité", "Problème Emballage", "Facturation", "Autre");

        generatedCaptcha = generateCaptcha();
        captchaField.setText(generatedCaptcha);

        captchaField.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            event.consume();
        });

        captchaField.addEventFilter(MouseEvent.MOUSE_PRESSED, MouseEvent::consume);
        captchaField.addEventFilter(MouseEvent.MOUSE_RELEASED, MouseEvent::consume);
    }

    private String generateCaptcha() {
        Random random = new Random();
        int captchaLength = 6;
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < captchaLength; i++) {
            int charType = random.nextInt(3);

            switch (charType) {
                case 0:
                    char randomUppercaseChar = (char) (random.nextInt(26) + 'A');
                    captcha.append(randomUppercaseChar);
                    break;
                case 1:
                    char randomLowercaseChar = (char) (random.nextInt(26) + 'a');
                    captcha.append(randomLowercaseChar);
                    break;
                case 2:
                    int randomNumber = random.nextInt(10);
                    captcha.append(String.valueOf(randomNumber));
                    break;
            }
        }

        return captcha.toString();
    }

    private boolean containsBadWords(String text) {
        for (String badWord : badWordsList) {
            if (text.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
