package controllers;

import Services.RestaurantData;
import Services.RestaurantService;
import entities.Restaurant;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.print.DocFlavor;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class ListRestaurantsController {


    public Button modifierRestoButton;
    public Button supprimerRestoButton;
    public Button ajouteRestoButton;
    public TextField searchField;
    public Button pdfRestoButton;


    @FXML
    private TableColumn<Restaurant, Integer> idColumn;
    @FXML
    private TableView<Restaurant> restaurantsTableView;

    @FXML
    private TableColumn<Restaurant, String> nomColumn;

    @FXML
    private TableColumn<Restaurant, String> adresseColumn;

    @FXML
    private TableColumn<Restaurant, String> categorieColumn;
    @FXML
    private TableColumn<Restaurant, String> imgPathColumn;









    public void initialize() {
        // Set up columns for restaurantsTableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_restaurant"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_restaurant"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse_restaurant"));
        categorieColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurantCategory().getCategoryName()));
        imgPathColumn.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));


        // If you want to display images, set a custom cell factory for imgPathColumn

        imgPathColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imgSrc, boolean empty) {
                super.updateItem(imgSrc, empty);

                if (empty || imgSrc == null) {
                    setGraphic(null);
                } else {

                    // Load the image using the provided file path
                    Image image = new Image(new File(imgSrc).toURI().toString());

                    // Check if the image loading was successful
                    imageView.setImage(image);
                    imageView.setFitWidth(50); // Adjust the width as needed
                    imageView.setFitHeight(50); // Adjust the height as needed
                    setGraphic(imageView);
                }
            }
        });

        // Fetch the list of restaurants from the database
        RestaurantService restaurantService = new RestaurantService();
        List<Restaurant> restaurants = restaurantService.readAll();

        // Sort the list by ID in ascending order
        restaurants.sort(Comparator.comparingInt(Restaurant::getId_restaurant));

        // Set the sorted list of restaurants to the TableView
        restaurantsTableView.getItems().setAll(restaurants);

        // Set up a listener to handle restaurant selection
        restaurantsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleRestaurantSelection(newValue);
            }
        });

        // Set actions for buttons
        if (ajouteRestoButton != null) {
            ajouteRestoButton.setOnAction(this::addRestaurant);
        }
        if (modifierRestoButton != null) {
            modifierRestoButton.setOnAction(this::modifierRestaurant);
        }
        if (supprimerRestoButton != null) {
            supprimerRestoButton.setOnAction(this::supprimerRestaurant);
        }
    }
    public void pdf(ActionEvent event) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

            // Retrieve the list of all restaurants
            RestaurantService restaurantService = new RestaurantService();
            List<Restaurant> restaurantList = restaurantService.readAll();

            float startY = 600;
            float startX = 50;
            float lineHeight = 20;
            float columnWidth = 180;
            float y = startY;

            // Draw the line separating headers and data
            contentStream.moveTo(startX, y);
            contentStream.lineTo(startX + 3 * columnWidth, y);
            contentStream.stroke();
            y -= lineHeight;

            // Draw column lines in the first row
            contentStream.moveTo(startX, startY);
            contentStream.lineTo(startX, y);
            contentStream.moveTo(startX + columnWidth, startY);
            contentStream.lineTo(startX + columnWidth, y);
            contentStream.moveTo(startX + 2 * columnWidth, startY);
            contentStream.lineTo(startX + 2 * columnWidth, y);
            contentStream.moveTo(startX + 3 * columnWidth, startY);
            contentStream.lineTo(startX + 3 * columnWidth, y);
            contentStream.stroke();

            // Draw column titles
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, y);
            contentStream.showText("Nom");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Adresse");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Catégorie");
            contentStream.endText();
            y -= lineHeight;

            // Draw restaurant values
            for (Restaurant restaurant : restaurantList) {
                contentStream.addRect(startX, y, columnWidth, lineHeight);
                contentStream.addRect(startX + columnWidth, y, columnWidth, lineHeight);
                contentStream.addRect(startX + 2 * columnWidth, y, columnWidth, lineHeight);
                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX + 5, y + lineHeight / 2); // Offset to the right
                contentStream.showText(restaurant.getNom_restaurant());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX + columnWidth + 5, y + lineHeight / 2); // Offset to the right
                contentStream.showText(restaurant.getAdresse_restaurant());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX + 2 * columnWidth + 5, y + lineHeight / 2); // Offset to the right
                contentStream.showText(restaurant.getRestaurantCategory().getCategoryName());
                contentStream.endText();

                y -= lineHeight;
            }

            contentStream.close();
            File file = new File("Liste_Restaurants.pdf");
            document.save(file);
            document.close();

            // Open the PDF document with the default application
            if (    Desktop.isDesktopSupported() && file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Unable to open the PDF file.");
            }
        } catch (IOException e) {
            System.out.println("Error during PDF file generation or opening: " + e.getMessage());
        }
    }




    private void handleRestaurantSelection(Restaurant selectedRestaurant) {
        try {
            // Load the ListPlats.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPlats.fxml"));
            Parent root = loader.load();

            // Access the controller associated with ListPlats.fxml
            ListPlatsController platListController = loader.getController();

            // Pass the selected restaurant to the controller
            platListController.setSelectedRestaurant(selectedRestaurant);

            // Set up the new stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ListPlats.fxml: " + e.getMessage());
        }
    }


    @FXML
    public void addRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the new window
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading ajoutRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void refreshTableView() {
        // Fetch the list of restaurants from the database
        RestaurantService restaurantService = new RestaurantService();
        List<Restaurant> restaurants = restaurantService.readAll();

        // Sort the list by ID in ascending order
        restaurants.sort(Comparator.comparingInt(Restaurant::getId_restaurant));

        // Set the sorted list of restaurants to the TableView
        restaurantsTableView.getItems().setAll(restaurants);
    }


    @FXML
    public void modifierRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the "Modifier" window
            Stage modifierStage = new Stage();
            modifierStage.setScene(new Scene(root));
            modifierStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading modifierRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void supprimerRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/suppRestaurant.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the "Supprimer" window
            Stage supprimerStage = new Stage();
            supprimerStage.setScene(new Scene(root));
            supprimerStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

            // After the window is closed, refresh the TableView
            refreshTableView();

        } catch (IOException e) {
            System.err.println("Error loading suppRestaurant.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSearch(KeyEvent event) {
        String keyword = searchField.getText();
        List<Restaurant> searchResults = RestaurantData.searchRestaurantsByKeyword(keyword);
        restaurantsTableView.getItems().setAll(searchResults);
    }
}
