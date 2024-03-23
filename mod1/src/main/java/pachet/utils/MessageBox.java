package pachet.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageBox {
    public static void showMessage(Stage stage, Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.initOwner(stage);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}