package controllers;

import controllers.DashBoard;
import entities.Evenement;
import entities.Participation;
import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.scene.input.MouseEvent;
import service.EvenementService;
import service.ParticipationService;

import java.io.IOException;


public class AjouterParticipation {

    @FXML
    private Button Ajout;

    @FXML
    private TextField idEvent;

    @FXML
    private TextField idU;

    @FXML
    private Label labelMenu;

    @FXML
    private TextField nbrP;


    @FXML
    void Menu(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoard.fxml"));
        try {
            Parent root = loader.load();
            DashBoard controller = loader.getController();
            labelMenu.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



        @FXML
        void ajoutP(ActionEvent event) {
        try {
            String idE = idEvent.getText();
            String idu = idU.getText();
            String nbr = nbrP.getText();


            // Vérifier si les champs obligatoires ne sont pas vides
            if (idu.isEmpty() || idE.isEmpty() || nbr.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return; // Sortir de la méthode si les champs obligatoires sont vides
            }

            // Vérifier si les valeurs numériques sont valides
            int idEv = 0;
            int idUs = 0;
            int nbrPlace = 0;
            try {
                idEv = Integer.parseInt(idE);
                idUs = Integer.parseInt(idu);
                nbrPlace = Integer.parseInt(nbr);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez saisir des valeurs numériques valides pour la quantité, le prix et la catégorie.");
                alert.showAndWait();
                return; // Sortir de la méthode si les valeurs numériques ne sont pas valides
            }

            // Si toutes les validations sont passées, ajouter le produit
            ParticipationService ps = new ParticipationService();
            ps.ajouter(new Participation(idEv,idUs,nbrPlace));

            // Afficher une alerte d'information en cas de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Le produit a été ajouté avec succès.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        }


}
