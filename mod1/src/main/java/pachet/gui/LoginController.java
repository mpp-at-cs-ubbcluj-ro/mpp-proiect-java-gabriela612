package pachet.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pachet.service.Service;
import pachet.Start;
import pachet.utils.MessageBox;

import java.io.IOException;

public class LoginController {
    Service service;
    Stage stage;

    public void setService(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    @FXML
    TextField usernameField;
    @FXML
    TextField parolaField;

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String parola = parolaField.getText();
        Integer id_angajat = service.Login(username, parola);

        if (id_angajat == null) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Date incorecte", "Datele introduse sunt incorecte.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("gui/meciuri-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = this.stage;
        MeciuriController meciuriController = fxmlLoader.getController();
        meciuriController.setService(service, stage, id_angajat);
        //closeWindow();

        stage.setTitle("Main view");
        stage.setScene(scene);
        stage.show();
    }
}
