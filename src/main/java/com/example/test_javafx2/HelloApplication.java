package com.example.test_javafx2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class
HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuBack.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuBack.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene((Parent) fxmlLoader.load(), 520, 400);
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 700);
        //Scene scene = new Scene(fxmlLoader.load(), 994, 624);
        stage.setTitle("Wakkel Food");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) throws Exception {
        /*String destinataire = "guesmiaymen00@gmail.com";
        String sujet = "tessst";
        String contenu = "nchallah ta5tef nchallah lyoum";
        testmailing.SendMail(destinataire, sujet, contenu);*/
        launch();
    }
}