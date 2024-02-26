package Controller;

import entities.AvisPlat;
import entities.Plat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import service.ServiceAvisPlat;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AvisController implements Initializable {

    @FXML
    private Button add, update, delete;
    @FXML
    private TextField note, idplat;
    @FXML
    private TextArea comm;
    @FXML
    private DatePicker date;
    @FXML
    VBox Myvbox;
    int clickCount = 0;
    private final ServiceAvisPlat serviceAvisPlat = ServiceAvisPlat.getInstance();
    private List<AvisPlat> avisPlats;

    private int clickedIdPlat;
    private int clickedId;
    private String clickedPlatName;
    private float clickedNote;
    private String clickedCommentaire;
    private LocalDate clickedDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            avisPlats = serviceAvisPlat.recuperer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        add.setOnAction(event -> {
            AvisPlat avisPlat = new AvisPlat();
            Plat plat = new Plat();

            String idPlatText = idplat.getText();
            if (idPlatText.isEmpty()) {
                showAlert("ID Plat is required");
                return;
            }

            try {
                plat.setId(Integer.parseInt(idPlatText));
            } catch (NumberFormatException e) {
                showAlert("Invalid ID Plat. Please enter a valid number.");
                return;
            }

            String noteText = note.getText();
            if (noteText.isEmpty()) {
                showAlert("Note is required");
                return;
            }

            try {
                float noteValue = Float.parseFloat(noteText);
                if (noteValue < 0 || noteValue > 10){
                    showAlert("Note must be a number between 0 and 10");
                    return;
                }
                avisPlat.setNote(noteValue);
            } catch (NumberFormatException e) {
                showAlert("Invalid Note. Please enter a valid number.");
                return;
            }

            String commText = comm.getText();
            if (commText.isEmpty()) {
                showAlert("Commentaire is required");
                return;
            }
            avisPlat.setCommentaire(commText);

            LocalDate selectedDate = date.getValue();
            if (selectedDate == null) {
                showAlert("Date is required");
                return;
            }
            avisPlat.setDateAvis(selectedDate);

            try {
                serviceAvisPlat.ajouter(avisPlat, plat);
                showAlert("AvisPlat added successfully", Alert.AlertType.INFORMATION);
                clearFields();
                refreshAvisPlats(); // Refresh the displayed AvisPlats after adding
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error occurred while adding AvisPlat", Alert.AlertType.ERROR);
            }
        });

        displayAvisPlats();

        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AvisPlat avisPlat = new AvisPlat();
                avisPlat.setCommentaire(comm.getText());
                avisPlat.setNote(Float.parseFloat(note.getText()));
                try {
                    serviceAvisPlat.modifier(clickedId,avisPlat);
                    showAlert("AvisPlat updated successfully", Alert.AlertType.INFORMATION);
                    refreshAvisPlats(); // Refresh after update
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    serviceAvisPlat.supprimer(clickedId);
                    showAlert("AvisPlat deleted successfully", Alert.AlertType.INFORMATION);
                    refreshAvisPlats(); // Refresh after delete
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void displayAvisPlats() {
        Myvbox.getChildren().clear();

        for (AvisPlat avisPlat : avisPlats) {
            TitledPane titledPane = createTitledPane(avisPlat);
            Myvbox.getChildren().add(titledPane);
        }
    }

    private TitledPane createTitledPane(AvisPlat avisPlat) {
        String headerText = "Avis #" + avisPlat.getIdAvis();
        String contentText = "ID Plat: " + avisPlat.getPlat().getId() + "\n"
                + "Plat: " + avisPlat.getPlat().getName() + "\n"
                + "Note: " + avisPlat.getNote() + "\n"
                + "Commentaire: " + avisPlat.getCommentaire() + "\n"
                + "Date: " + avisPlat.getDateAvis();

        Label contentLabel = new Label(contentText);
        contentLabel.setWrapText(true);

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("ID Plat:"), 0, 0);
        gridPane.add(new Label(String.valueOf(avisPlat.getPlat().getId())), 1, 0);
        gridPane.add(new Label("Plat:"), 0, 1);
        gridPane.add(new Label(avisPlat.getPlat().getName()), 1, 1);
        gridPane.add(new Label("Note:"), 0, 2);
        gridPane.add(new Label(String.valueOf(avisPlat.getNote())), 1, 2);
        gridPane.add(new Label("Commentaire: "), 0, 3);
        gridPane.add(new Label(avisPlat.getCommentaire()), 1, 3);
        gridPane.add(new Label("Date: "), 0, 4);
        gridPane.add(new Label(String.valueOf(avisPlat.getDateAvis())), 1, 4);

        // Define a click count variable


        gridPane.setOnMouseClicked(event -> {
            clickCount++;

            // Set the variables when clicked
            clickedId = avisPlat.getIdAvis();
            clickedIdPlat = avisPlat.getPlat().getId();
            clickedPlatName = avisPlat.getPlat().getName();
            clickedNote = avisPlat.getNote();
            clickedCommentaire = avisPlat.getCommentaire();
            clickedDate = avisPlat.getDateAvis();
            System.out.println(clickedId);

            // Update the UI elements
            note.setText(String.valueOf(clickedNote));
            date.setValue(clickedDate);
            comm.setText(clickedCommentaire);
            idplat.setText(String.valueOf(clickedIdPlat));

            idplat.setDisable(true);
            date.setDisable(true);

            if (clickCount == 2) {
                idplat.setDisable(false);
                date.setDisable(false);
            }

            // Reset the click count after 2 clicks
            if (clickCount == 2) {
                clickCount = 0;
            }
        });


        TitledPane titledPane = new TitledPane();
        titledPane.setText(headerText);
        titledPane.setContent(gridPane);
        titledPane.setExpanded(false);

        return titledPane;
    }

    private void refreshAvisPlats() {
        try {
            avisPlats = serviceAvisPlat.recuperer();
            displayAvisPlats();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error refreshing AvisPlats", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        showAlert(message, Alert.AlertType.ERROR);
    }

    private void clearFields() {
        idplat.clear();
        note.clear();
        comm.clear();
        date.setValue(null);
    }
}
