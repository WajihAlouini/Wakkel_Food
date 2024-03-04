package com.example.test_javafx2 ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entities.evaluation;
import service.evaluationservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AjouterEvaluation {

    @FXML
    private TextArea commentaireField;

    @FXML
    private ChoiceBox<Integer> notefield;

    @FXML
    private TextField captchaField;

    @FXML
    private TextField userCaptchaInput;

    private String generatedCaptcha;
    private List<String> badWordsList;

    public AjouterEvaluation() {
        badWordsList = new ArrayList<>();
        badWordsList.add("badword1");
        badWordsList.add("badword2");
        // Add more bad words as needed
    }

    @FXML
    void ajouterEvaluation(ActionEvent event) {
        String commentaire = commentaireField.getText();
        Integer selectedNote = notefield.getValue();
        String enteredCaptcha = userCaptchaInput.getText();
        String email_c = com.example.test_javafx2.UserSession.getInstance().getEmail();

        if (containsBadWords(commentaire)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le commentaire contient des mots inappropriés.");
            alert.show();
            return;
        }

        if (commentaire != null && !commentaire.trim().isEmpty() && selectedNote != null) {
            if (enteredCaptcha.equals(generatedCaptcha)) {
                Date currentDate = new Date(System.currentTimeMillis());
                evaluation evaluationx = new evaluation(0,email_c , currentDate, selectedNote, commentaire);

                evaluationservice evaluationservicex = new evaluationservice();

                try {
                    evaluationservicex.add(evaluationx);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Evaluation ajoutée avec succès");
                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erreur lors de l'ajout de l'évaluation.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Le captcha saisi est incorrect.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le champ commentaire ne peut pas être vide.");
            alert.show();
        }
    }

    @FXML
    void initialize() {
        ObservableList<Integer> noteOptions = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        notefield.setItems(noteOptions);
        notefield.setValue(1);

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
