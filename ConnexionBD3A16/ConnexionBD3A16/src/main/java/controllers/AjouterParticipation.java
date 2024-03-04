package controllers;

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
import service.EmailService;
import service.ParticipationService;

import java.io.IOException;
import java.security.SecureRandom;




public class AjouterParticipation {

    private static final int CODE_LENGTH =4 ;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @FXML
    private Button Ajout;


    @FXML
    private TextField idU;

    @FXML
    private TextField idE;

    @FXML
    private Label labelMenu;

    @FXML
    private TextField nbrP;
    @FXML
    private TextField emailU;
private  int idEvent ;

    public void setIdEvent(int idEvent) {
        this.idEvent=idEvent;

    }
    @FXML
    void Menu(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front.fxml"));
        try {
            Parent root = loader.load();
            front controller = loader.getController();
            labelMenu.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    @FXML
    void ajoutP(ActionEvent event) {
        try {

            String idu = idU.getText();
            String ide = idE.getText();
            String nbr = nbrP.getText();
            String emailUser = emailU.getText(); // Replace with the actual ID of your email TextField
           int id = Integer.parseInt(ide);
            // Vérifier si les champs obligatoires ne sont pas vides
            if (idu.isEmpty() || nbr.isEmpty() || emailUser.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return; // Sortir de la méthode si les champs obligatoires sont vides
            }

            //int idEv = 0;
            int idUs = 0;
            int nbrPlace = 0;
            try {

                idUs = Integer.parseInt(idu);
                nbrPlace = Integer.parseInt(nbr);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez saisir des valeurs numériques valides ");
                alert.showAndWait();
                return; // Sortir de la méthode si les valeurs numériques ne sont pas valides
            }

            // Si toutes les validations sont passées, ajouter le produit
            ParticipationService ps = new ParticipationService();
            ps.ajouter(new Participation(idUs,id, nbrPlace, emailUser));
            EmailService.sendEmail(emailUser, "Participation Confirmation", "Thank you for participating!");
            // Afficher une alerte d'information en cas de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("La participation a été ajouté avec succès.");
            alert.showAndWait();
            if (ps.nombreParticipationsUtilisateur(idUs) == 3) {
                // Générez un code promo aléatoire
                String codePromo = genererCodePromo();
                System.out.println("Code promo généré pour l'utilisateur " + idUs + ": " + codePromo);

// Show an alert with the generated promo code
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Code Promo Généré");
                alert.setHeaderText(null);
                alert.setContentText("Code promo généré pour l'utilisateur " + idUs + ": " + codePromo);
                alert.showAndWait();
                // Vous pouvez stocker le code promo dans la base de données ou l'envoyer à l'utilisateur, selon vos besoins
            }

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void initData(int selectedEventId) {

    }

    private String genererCodePromo() {
        SecureRandom random = new SecureRandom();
        StringBuilder codePromo = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            codePromo.append(randomChar);
        }

        return codePromo.toString();
    }
}