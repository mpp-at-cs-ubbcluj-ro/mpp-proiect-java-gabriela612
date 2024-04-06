package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.repository.*;
import org.example.service.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainStart extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Properties props=new Properties();
        try {
            props.load(new FileReader("../bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        IMeciRepository meciRepository = new MeciDBRepository(props);
        IAngajatRepository angajatRepository = new AngajatDBRepository(props);
        IBiletRepository biletRepository = new BiletDBRepository(props);
        Service service = new Service(angajatRepository, meciRepository, biletRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(MainStart.class.getResource("/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service, stage);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.show();
    }
}
