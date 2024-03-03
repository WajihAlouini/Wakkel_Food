package com.example.test_javafx2;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import service.clientService;
import entities.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientBackController implements Initializable {

    @FXML
    private ImageView backgroundcompteImageView;
    @FXML
    private ImageView rechercheImageView;
    @FXML
    private TextField rechercheTextField;
    @FXML
    private TableColumn<client, String> nomTableColumn;
    @FXML
    private TableColumn<client, String> prenomTableColumn;
    @FXML
    private TableColumn<client, String> emailTableColumn;
    @FXML
    private TableColumn<client, String> mdpTableColumn;
    @FXML
    private TableColumn<client, String> numeroTableColumn;
    @FXML
    private TableColumn<client, String> adresseTableColumn;
    @FXML
    private TableColumn<client, client.GenreEnum> genreTableColumn;
    @FXML
    private TableColumn<client, String> imageTableColumn;
    @FXML
    private TableColumn<client, String> editColumn;
    @FXML
    private TableView<client> clientTableView;
    @FXML
    private TableColumn<client, Void> banColumn;
    private final clientService clientService = new clientService();
    private ObservableList<client> observableClientList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundcompteFile = new File("images/backgroundcompte.jpg");
        Image backgroundcompteImage = new Image(backgroundcompteFile.toURI().toString());
        backgroundcompteImageView.setImage(backgroundcompteImage);
        File rechercheFile = new File("images/recherche.png");
        Image rechercheImage = new Image(rechercheFile.toURI().toString());
        rechercheImageView.setImage(rechercheImage);

        List<client> clientList = clientService.readAll();
        List<client> filteredClientList = clientList.stream()
                .filter(client -> client.getRole() == entities.client.RoleEnum.client)
                .collect(Collectors.toList());
        observableClientList = FXCollections.observableArrayList(filteredClientList);

        rechercheTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Task<List<client>> searchTask = new Task<List<client>>() {
                @Override
                protected List<client> call() throws Exception {
                    return observableClientList.stream()
                            .filter(client -> clientMatchesSearch(client, newValue))
                            .collect(Collectors.toList());
                }
            };

            searchTask.setOnSucceeded(event -> {
                List<client> filteredList = searchTask.getValue();
                Platform.runLater(() -> clientTableView.setItems(FXCollections.observableArrayList(filteredList)));
            });

            Thread searchThread = new Thread(searchTask);
            searchThread.setDaemon(true);
            searchThread.start();
        });

        banColumn.setCellFactory(param -> new TableCell<client, Void>() {
            final Button banButton = new Button();

            {
                addBanHoverAnimation(banButton);
                banButton.setOnAction(event -> {
                    client client = getTableView().getItems().get(getIndex());
                    toggleBanStatus(client);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    client client = getTableView().getItems().get(getIndex());
                    boolean isBanned = clientService.isClientBanned(client.getEmail());
                    banButton.setText(isBanned ? "Débloquer" : "Bloquer");
                    setGraphic(banButton);
                }
            }
        });

        imageTableColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageTableColumn.setCellFactory(column -> new TableCell<client, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new File(imagePath).toURI().toString());
                        imageView.setImage(image);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nomTableColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomTableColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mdpTableColumn.setCellValueFactory(new PropertyValueFactory<>("newpassword"));
        numeroTableColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        adresseTableColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        genreTableColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreTableColumn.setCellFactory(column -> new TableCell<client, client.GenreEnum>() {
            @Override
            protected void updateItem(client.GenreEnum genre, boolean empty) {
                super.updateItem(genre, empty);
                if (empty || genre == null) {
                    setText(null);
                } else {
                    setText(genre.toString());
                }
            }
        });

        editColumn.setCellFactory(param -> new TableCell<client, String>() {
            final Button deleteButton = new Button("Supprimer");
            final Button editButton = new Button("Modifier");

            {
                deleteButton.setOnAction(event -> deleteClient(getTableView().getItems().get(getIndex())));
                editButton.setOnAction(event -> editClient(getTableView().getItems().get(getIndex())));
                addButtonHoverAnimation(editButton);
                addsupprimerHoverAnimation(deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
                    editButton.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white;");
                    setGraphic(new HBox(deleteButton, editButton));
                }
            }
        });

        clientTableView.setItems(observableClientList);
    }

    private boolean clientMatchesSearch(client client, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        return client.getNom().toLowerCase().contains(searchText.toLowerCase())
                || client.getPrenom().toLowerCase().contains(searchText.toLowerCase())
                || client.getEmail().toLowerCase().contains(searchText.toLowerCase())
                || client.getNewpassword().toLowerCase().contains(searchText.toLowerCase())
                || client.getNumero().toLowerCase().contains(searchText.toLowerCase())
                || client.getAdresse().toLowerCase().contains(searchText.toLowerCase())
                || client.getGenre().toString().toLowerCase().contains(searchText.toLowerCase());
    }

    private void toggleBanStatus(client client) {
        boolean isBanned = clientService.isClientBanned(client.getEmail());
        clientService.banClient(client.getEmail(), !isBanned);
        clientTableView.refresh();
    }

    private void deleteClient(client client) {
        String email = client.getEmail();
        clientService.delete(email);
        clientTableView.getItems().remove(client);
    }

    private void editClient(client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminClient.fxml"));
            Parent parent = loader.load();
            AdminClientController editClientController = loader.getController();
            editClientController.initData(client);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ClientBackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void addButtonHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #2980b9;-fx-text-fill: white;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #0000FF;-fx-text-fill: white;");
        });
    }
    private void addsupprimerHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #ef006c;-fx-text-fill: white;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-background-color: #FF0000;-fx-text-fill: white;");
        });
    }
    private void addBanHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.play();
            button.setStyle("-fx-background-color: #FF0000;-fx-text-fill: white;");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle("-fx-text-fill: #000000;");
        });
    }
}
