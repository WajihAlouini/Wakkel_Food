package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DashBoard {

    @FXML
    private Label labelAdd;

    @FXML
    private Label labelList;
    @FXML
    private Label labelPart;
    @FXML
    private Label labelPartc;
    @FXML
    void AjouterEvent(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
        try {
            Parent root = loader.load();
            AjouterEvenement controller = loader.getController();
            labelAdd.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void listE(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
        try {
            Parent root = loader.load();
            AfficherEvenement controller = loader.getController();
            labelList.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void ajoutP(MouseEvent event) {

    }
    @FXML
    void MenuP(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterParticipation.fxml"));
        try {
            Parent root = loader.load();
            AjouterParticipation controller = loader.getController();
            labelPart.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void MenuPa(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipation.fxml"));
        try {
            Parent root = loader.load();
            AfficherParticipation controller = loader.getController();
            labelPartc.getScene().setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}