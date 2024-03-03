    package controllers;

    import Services.PlatData;
    import Services.PlatService;
    import Services.RestaurantData;
    import javafx.scene.control.Button;
    import javafx.scene.control.TextField;
    import org.apache.pdfbox.pdmodel.PDDocument;
    import org.apache.pdfbox.pdmodel.PDPage;
    import org.apache.pdfbox.pdmodel.PDPageContentStream;
    import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
    import org.apache.pdfbox.pdmodel.common.PDRectangle;
    import org.apache.pdfbox.pdmodel.font.PDType1Font;
    import com.sun.javafx.charts.Legend;
    import entities.Plat;
    import entities.Restaurant;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyEvent;
    import javafx.stage.Stage;

    import java.awt.*;
    import java.io.File;
    import java.io.IOException;
    import java.net.MalformedURLException;
    import java.util.List;

    public class ListPlatsController {
        public Restaurant selectedRestaurant;
        public TableColumn<Plat , String> imgPathColumn;
        public ImageView listplatbg;
        public Button ajouterPlatButton;
        public Button modifierPlatButton;
        public Button supprimerPlatButton;
        public TextField searchField;
        public Button pdfPlatButton;
        @FXML
        private TableView<Plat> platsTableView;

        @FXML
        private TableColumn<Plat, Integer> idColumn1;

        @FXML
        private TableColumn<Plat, String> nomColumn1;

        @FXML
        private TableColumn<Plat, Double> prixColumn;

        @FXML
        private TableColumn<Plat, String> ingredientColumn;



        // Reference to the selected restaurant


        public void setSelectedRestaurant(Restaurant selectedRestaurant) {
            this.selectedRestaurant = selectedRestaurant;
            // Update the platsTableView with the plats of the selected restaurant
            refreshTableView();
        }
        public void pdf(ActionEvent event) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);

                // Retrieve the list of all plates
                PlatService plateService = new PlatService();
                List<Plat> plateList = plateService.readAll();

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
                contentStream.showText("Prix");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText("Ingrédients");
                contentStream.endText();
                y -= lineHeight;

                // Draw plate values
                for (Plat plat : plateList) {
                    contentStream.addRect(startX, y, columnWidth, lineHeight);
                    contentStream.addRect(startX + columnWidth, y, columnWidth, lineHeight);
                    contentStream.addRect(startX + 2 * columnWidth, y, columnWidth, lineHeight);
                    contentStream.stroke();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(startX + 5, y + lineHeight / 2); // Offset to the right
                    contentStream.showText(plat.getNom_plat());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(startX + columnWidth + 5, y + lineHeight / 2); // Offset to the right
                    contentStream.showText(String.valueOf(plat.getPrix()));
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(startX + 2 * columnWidth + 5, y + lineHeight / 2); // Offset to the right

                    // Assuming getIngredient() returns a single string
                    String ingredient = plat.getIngredient();
                    contentStream.showText(ingredient != null ? ingredient : "N/A");

                    contentStream.endText();

                    y -= lineHeight;
                }

                contentStream.close();
                File file = new File("Liste_Plats.pdf");
                document.save(file);
                document.close();

                // Open the PDF document with the default application
                if (Desktop.isDesktopSupported() && file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("Unable to open the PDF file.");
                }
            } catch (IOException e) {
                System.out.println("Error during PDF file generation or opening: " + e.getMessage());
            }
        }


        @FXML
        public void addPlat() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutPlat.fxml"));
                Parent root = loader.load();

                // Create a new Stage for the new window
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

                // After the window is closed, refresh the TableView
                refreshTableView();

            } catch (IOException e) {
                System.err.println("Error loading ajoutPlat.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        public void modifierPlat() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPlat.fxml"));
                Parent root = loader.load();

                // Create a new Stage for the new window
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

                // After the window is closed, refresh the TableView
                refreshTableView();

            } catch (IOException e) {
                System.err.println("Error loading modifierPlat.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        public void supprimerPlat() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/suppPlat.fxml"));
                Parent root = loader.load();

                // Create a new Stage for the new window
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.showAndWait(); // Use showAndWait to wait for the new window to be closed

                // After the window is closed, refresh the TableView
                refreshTableView();

            } catch (IOException e) {
                System.err.println("Error loading supprimerPlat.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }




        @FXML
        public void initialize() {
            // Set up columns for platsTableView
            idColumn1.setCellValueFactory(new PropertyValueFactory<>("id_plat"));
            nomColumn1.setCellValueFactory(new PropertyValueFactory<>("nom_plat"));
            prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
            imgPathColumn.setCellValueFactory(new PropertyValueFactory<>("pimgSrc"));
            imgPathColumn.setCellFactory(column -> new TableCell<>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String pimgSrc, boolean empty) {
                    super.updateItem(pimgSrc, empty);

                    if (empty || pimgSrc == null) {
                        setGraphic(null);
                    } else {

                        // Load the image using the provided file path
                        Image image = new Image(new File(pimgSrc).toURI().toString());

                        // Check if the image loading was successful
                        imageView.setImage(image);
                        imageView.setFitWidth(50); // Adjust the width as needed
                        imageView.setFitHeight(50); // Adjust the height as needed
                        setGraphic(imageView);
                    }
                }
            });
            // Set up a listener to handle plat selection (if needed)
            platsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    handlePlatSelection();
                }
            });
        }
        @FXML
        private void handleSearch(KeyEvent event) {
            String keyword = searchField.getText();
            List<Plat> searchResults = PlatData.searchPlatsByKeyword(keyword);
            platsTableView.getItems().setAll(searchResults);
        }

        private void handlePlatSelection() {
            // Handle the selection of a plat, if needed
            // For example, you can open a new window to display more details about the selected plat
        }

        private void refreshTableView() {
            // Fetch the updated list of plats for the selected restaurant from the database
            if (selectedRestaurant != null) {
                PlatService platService = new PlatService();
                List<Plat> plats = platService.getPlatsByRestaurantId(selectedRestaurant.getId_restaurant());

                // Set the sorted list of plats to the TableView
                updateData(plats);
            }
        }

        private void updateData(List<Plat> plats) {
            // Update the data in platsTableView
            ObservableList<Plat> platObservableList = FXCollections.observableArrayList(plats);
            platsTableView.setItems(platObservableList);
        }


    }
