package pachet;

import javafx.scene.control.Alert;
import pachet.gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pachet.repository.*;

import pachet.service.Service;
import pachet.utils.MessageBox;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Start extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }


        IAngajatRepository angajatRepository = new AngajatDBRepository(props);
        IMeciRepository meciRepository = new MeciDBRepository(props);
        IBiletRepository biletRepository = new BiletDBRepository(props);

        Service service = new Service(angajatRepository, meciRepository, biletRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("gui/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service, stage);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
