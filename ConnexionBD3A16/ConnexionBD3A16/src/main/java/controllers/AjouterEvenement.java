package controllers;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.EvenementService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static java.lang.Integer.*;
import static java.sql.Date.valueOf;


public class AjouterEvenement {

    @FXML
    private TextField EventN;

    @FXML
    private Button ajout;

    @FXML
    private DatePicker dateD;

    @FXML
    private DatePicker dateF;


    @FXML
    private Label labelMenu;

    @FXML
    void Menu(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
        try {
            Parent root = loader.load();
            back controller = loader.getController();
            labelMenu.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML

    private java.sql.Date DateFin, DateDebut;

    @FXML
    void ajouterE(ActionEvent event) {
            try {
                String eventName = EventN.getText();
                LocalDate startDate = dateD.getValue();
                LocalDate endDate = dateF.getValue();


                // Vérifier si les champs obligatoires ne sont pas vides
                if (eventName.isEmpty() ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Veuillez remplir tous les champs obligatoires.");
                    alert.showAndWait();
                    return;
                }
              
                // Si toutes les validations sont passées, ajouter l'événement
                Evenement evenement = new Evenement( eventName, startDate, endDate);
                EvenementService evenementService = new EvenementService();
                evenementService.ajouter(evenement);

                // Afficher une alerte d'information en cas de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("L'événement a été ajouté avec succès.");
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez saisir un code promo valide.");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }


    }

   
    

}