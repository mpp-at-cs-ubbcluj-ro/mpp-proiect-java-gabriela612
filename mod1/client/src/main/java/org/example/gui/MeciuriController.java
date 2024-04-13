package org.example.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.LoginController;
import org.example.MainStart;
import org.example.observer.IObserver;
import org.example.services.IServices;
import org.example.utils.*;
import org.example.domain.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MeciuriController implements Initializable, IObserver {
    IServices service;
    Stage stage;
    int idAngajat;

    ObservableList<MeciL> model = FXCollections.observableArrayList();

    public TableView<MeciL> meciuriTable;
    public TableColumn<MeciL, String> numeColumn;
    public TableColumn<MeciL, Integer> pretBiletColumn;
    public TableColumn<MeciL, LocalDate> dataColumn;
    public TableColumn<MeciL, String> nrLocuriDisponibileColumn;
    public TextField numeClientField;
    public Spinner<Integer> nrLocuriSpinner;
    private boolean filtru = false;
    public Button buttonFiltru;

    public MeciuriController() {
    }

    public MeciuriController(IServices service) {
        this.service = service;
    }

    public void setService(IServices service) {
        this.service = service;
    }

    public void initData() {
        List<MeciL> meciuri;
        if (!filtru) {
            meciuri = (List<MeciL>) service.getMeciuri();
            this.buttonFiltru.setText("Doar meciurile la care mai sunt bilete");
        }
        else {
            meciuri = (List<MeciL>) service.getMeciuriLibere();
            this.buttonFiltru.setText("Toate meciurile");
        }
        model.setAll(meciuri);
    }

    @FXML
    public void initialize(){
        nrLocuriDisponibileColumn.setCellValueFactory(new PropertyValueFactory<>("nrLocuriDisponibile"));
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
        try {
            service.logout(idAngajat);
        } catch (Exception e) {
            System.out.println("Logout error " + e);
        }
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
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
        MeciL meciL = meciuriTable.getSelectionModel().getSelectedItem();
        if (meciL == null) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Selection error", "Nu ati selectat meciul");
            return;
        }

        Meci meci = meciL;

        Bilet bilet = service.cumparaBilet(meci, numeClient, nrLocuri);
        if (bilet == null) {
            MessageBox.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "S-a produs o eroare neasteptata. Va rog sa incercati mai tarziu.");
            return;
        }

        MessageBox.showMessage(null, Alert.AlertType.INFORMATION,
                "Succes", "Cumpararea biletului a fost inregistrata in baza de date.");
    }


    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("INIT : " + meciuriTable);

        System.out.println("END INIT!!!!!!!!!");
    }

    @Override
    public void schimbareMeciuri(Iterable<MeciL> meciuri) {
        Platform.runLater(()->{
            List<MeciL> meciuri_noi = (List<MeciL>) meciuri;
            model.setAll(meciuri_noi);
            System.out.println("New meciuri list controller schimbareMeciuri " + meciuri);
        });
    }
    public void setIdAngajat(Integer idAngajat) {
        this.idAngajat = idAngajat;
    }
}
