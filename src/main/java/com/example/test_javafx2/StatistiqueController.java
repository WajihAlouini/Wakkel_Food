package com.example.test_javafx2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.clientService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import entities.client;


public class StatistiqueController implements Initializable {

    @FXML
    private ImageView statistiquebackgroundImageView;

    @FXML
    private PieChart statistiquePieChart;

    private clientService service = new clientService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File statistiquebackgroundFile = new File("images/statistiquebackground.jpg");
        Image statistiquebackgroundImage = new Image(statistiquebackgroundFile.toURI().toString());
        statistiquebackgroundImageView.setImage(statistiquebackgroundImage);

        // Récupérer les données sur le nombre d'hommes et de femmes
        long countHommes = service.countByGenre(entities.client.GenreEnum.homme);
        long countFemmes = service.countByGenre(entities.client.GenreEnum.femme);

        // Créer les données pour le PieChart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Hommes", countHommes),
                        new PieChart.Data("Femmes", countFemmes)
                );

        // Ajouter les données au PieChart
        statistiquePieChart.setData(pieChartData);
    }
}
