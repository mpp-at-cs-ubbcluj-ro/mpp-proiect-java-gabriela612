package pachet.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pachet.Start;
import pachet.domain.Bilet;
import pachet.domain.Meci;
import pachet.service.Service;
import pachet.utils.MessageBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MeciuriController {
    Service service;
    Stage stage;
    int idAngajat;

    ObservableList<Meci> model = FXCollections.observableArrayList();

    public TableView<Meci> meciuriTable;
    public TableColumn<Meci, String> numeColumn;
    public TableColumn<Meci, Integer> pretBiletColumn;
    public TableColumn<Meci, LocalDate> dataColumn;
    public TableColumn<Meci, String> nrLocuriDisponibileColumn;
    public TextField numeClientField;
    public Spinner<Integer> nrLocuriSpinner;
    private boolean filtru = false;
    public Button buttonFiltru;

    public void setService(Service service, Stage stage, int idAngajat) {
        this.service = service;
        this.stage = stage;
        this.idAngajat = idAngajat;
        initData();
    }

    public void initData() {
        List<Meci> meciuri;
        if (!filtru) {
            meciuri = (List<Meci>) service.getMeciuri();
            this.buttonFiltru.setText("Doar meciurile la care mai sunt bilete");
        }
        else {
            meciuri = (List<Meci>) service.getMeciuriLibere();
            this.buttonFiltru.setText("Toate meciurile");
        }
        model.setAll(meciuri);
    }

    @FXML
    public void initialize(){
        nrLocuriDisponibileColumn.setCellValueFactory(entity -> {
            int nr = service.nrLocuriDisponibileMeci(entity.getValue());
            if (nr != 0)
                return new SimpleObjectProperty<>(String.valueOf(nr));
            return new SimpleObjectProperty<>("SOLD OUT");
        });
        numeColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        pretBiletColumn.setCellValueFactory(new PropertyValueFactory<>("pretBilet"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        nrLocuriSpinner.setValueFactory(valueFactory);

        meciuriTable.setItems(model);
    }

    @FXML
    public void handleFiltrare(ActionEvent actionEvent) {
        this.filtru = !filtru;
        initData();
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("gui/login-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = this.stage;
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service, stage);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleVanzare(ActionEvent actionEvent) {
        Integer nrLocuri = nrLocuriSpinner.getValue();
        if (nrLocuri == 0) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Date incomplete", "Nu ati selectat numarul de locuri");
            return;
        }
        String numeClient = numeClientField.getText();
        if (numeClient == null || Objects.equals(numeClient, "")) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Date incomplete", "Nu ati completat numele clientului");
            return;
        }
        Meci meci = meciuriTable.getSelectionModel().getSelectedItem();
        if (meci == null) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Selection error", "Nu ati selectat meciul");
            return;
        }
        Bilet bilet = service.cumparaBilet(meci, numeClient, nrLocuri);
        if (bilet == null) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "S-a produs o eroare neasteptata. Va rog sa incercati mai tarziu.");
            return;
        }
        MessageBox.showMessage(null, Alert.AlertType.INFORMATION,
                "Succes", "Cumpararea biletului a fost inregistrata in baza de date.");
    }

}
