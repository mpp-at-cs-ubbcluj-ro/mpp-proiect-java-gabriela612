package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.domain.Meci;
import org.example.gui.MeciuriController;
import org.example.networking.ProtoServicesProxy;
import org.example.networking.ServicesProxy;
import org.example.services.IServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainStart extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55556;
    private static String defaultServer = "127.0.0.1";
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

//        Properties props=new Properties();
//        try {
//            props.load(new FileReader("../bd.config"));
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config "+e);
//        }

        primaryStage = stage;

        Properties clientProps = new Properties();
        try {
            clientProps.load(MainStart.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new ProtoServicesProxy(serverIP, serverPort);



//        IMeciRepository meciRepository = new MeciDBRepository(props);
//        IAngajatRepository angajatRepository = new AngajatDBRepository(props);
//        IBiletRepository biletRepository = new BiletDBRepository(props);
//        Service service = new Service(angajatRepository, meciRepository, biletRepository);

//        FXMLLoader fxmlLoader = new FXMLLoader(MainStart.class.getResource("/login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        LoginController loginController = fxmlLoader.getController();
//        loginController.setService(server, stage);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//
//        stage.show();


        FXMLLoader loader = new FXMLLoader(
                MainStart.class.getResource("/login-view.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setService(server, primaryStage);




        FXMLLoader cloader = new FXMLLoader(
                MainStart.class.getResource("/meciuri-view.fxml"));
        Parent croot=cloader.load();


        MeciuriController meciuriCtrl =
                cloader.<MeciuriController>getController();
        meciuriCtrl.setService(server);

        ctrl.setController(meciuriCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
