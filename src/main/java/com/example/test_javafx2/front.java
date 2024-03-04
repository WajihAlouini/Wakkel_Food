package com.example.test_javafx2;

import entities.Evenement;
import entities.Participation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.DataSource;

import java.io.IOException;
import java.sql.*;

public class front {
    @FXML
    private Label toggleLabel;


    @FXML
    private TableColumn<?, ?> emailU;
    @FXML
    private TableColumn<?, ?> nbrP;
    @FXML
    private javafx.scene.control.TableView<Evenement> TableView;

    @FXML
    private javafx.scene.control.TableView<Participation> TableView1;
    @FXML
    private TableColumn<Evenement, Integer> idEvent;

    @FXML
    private TableColumn<Evenement, String> eventName;

    @FXML
    private TableColumn<Evenement, Date> DateD;

    @FXML
    private TableColumn<Evenement, Date> DateF;
    @FXML
    private Button addButton;
    Connection cnx = DataSource.getInstance().getCnx();

    public void initialize() {
        TableView1.setVisible(false);
        ObservableList<Evenement> items = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM evenement";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvent(rs.getInt(1));
                evenement.setEventName(rs.getString(2));
                evenement.setDateDebut(Date.valueOf(rs.getDate("dateDebut").toLocalDate()));
                evenement.setDateFin(Date.valueOf(rs.getDate("dateFin").toLocalDate()));
                items.add(evenement);
            }



            // Bind the new columns to the corresponding properties
            idEvent.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
            eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
            DateD.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            DateF.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

            // Set the items to the TableView
            TableView.setItems(items);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addButton.setOnAction(this::handleAddButtonClick);

        ObservableList<Participation> participationItems = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM participation";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Participation participation = new Participation();
                participation.setNbrPlace(rs.getInt(4));
                participation.setEmailUser(rs.getString(5));

                participationItems.add(participation); // Fix the list to add to participationItems, not items
            }

            nbrP.setCellValueFactory(new PropertyValueFactory<>("nbrPlace"));
            emailU.setCellValueFactory(new PropertyValueFactory<>("emailUser"));

            // Set the items to the TableView
            TableView1.setItems(participationItems); // Fix the TableView to use TableView1

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void toggleTableViewVisibility(MouseEvent event) {
        TableView1.setVisible(!TableView1.isVisible());
    }

    private void handleAddButtonClick(ActionEvent event) {
        // Get the selected item from the TableView
        Evenement selectedEvent = TableView.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // You can open "AjouterParticipation.fxml" here and pass the necessary data if needed
            openAjouterParticipationStage(selectedEvent.getIdEvent());
        } else {
            // Handle case when no item is selected
            System.out.println("No item selected.");
        }
    }

    // Method to open "AjouterParticipation.fxml" stage
    private void openAjouterParticipationStage(int selectedEventId) {
        try {
            // Load "AjouterParticipation.fxml" using FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterParticipation.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            AjouterParticipation ajouterParticipation = loader.getController();

            // Pass the selected eventId to the AjouterParticipationController
            ajouterParticipation.initData(selectedEventId);

            // Set up the stage
            Stage ajouterParticipationStage = new Stage();
            ajouterParticipationStage.setTitle("Ajouter Participation");
            ajouterParticipationStage.setScene(new Scene(root));

            // Show the stage
            ajouterParticipationStage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}