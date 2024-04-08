package org.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.domain.Angajat;
import org.example.gui.MeciuriController;
import org.example.services.IServices;
import org.example.utils.*;
import org.example.MainStart;

import java.io.IOException;

public class LoginController {
    IServices service;
    private MeciuriController meciuriCtrl;
    private Angajat angajat;
    @FXML
    TextField user;
    @FXML
    TextField password;

    Parent mainChatParent;

    public void setService(IServices service, Stage stage) {
        this.service = service;
    }

    @FXML
    TextField usernameField;
    @FXML
    TextField parolaField;

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String parola = parolaField.getText();
        angajat = new Angajat(parola, username);


        try {
            this.angajat.setId(service.login(username, parola, meciuriCtrl));
            // Util.writeLog("User succesfully logged in "+crtUser.getId());
            Stage stage = new Stage();
            stage.setTitle("Meciuri Window for " + username);
            stage.setScene(new Scene(mainChatParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    meciuriCtrl.handleLogout(null);
                    System.exit(0);
                }
            });

            stage.show();
            meciuriCtrl.setIdAngajat(angajat.getId());
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            meciuriCtrl.initData();
            meciuriCtrl.initialize();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }

    }
    public void setUser(Angajat angajat) {
        this.angajat = angajat;
    }

    public void setController(MeciuriController meciuriController) {
        this.meciuriCtrl = meciuriController;
    }

    public void setParent(Parent p) {
        mainChatParent = p;
    }
}
