package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Choix {

    @FXML
    void onReclamerButtonClick(ActionEvent event) {
        loadFXML("/AjouterReclamation.fxml");
    }

    @FXML
    void onEvaluerButtonClick(ActionEvent event) {
        loadFXML("/AjouterEvaluation.fxml");
    }

    @FXML
    void onListeRButtonClick(ActionEvent event) {
        loadFXML("/ListeReclamation.fxml");
    }

    @FXML
    void onListeEButtonClick(ActionEvent event) {loadFXML("/ListeEvaluation.fxml");}



    private void loadFXML(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
