package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.SalesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesClientListController implements Initializable, DataChangeListener {

    private Sales sales;

    private ClientService clientService;

    private SalesFormController salesFormController;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, String> tableColumnId;

    @FXML
    private TableColumn<Client, String> tableColumnName;

    private ObservableList<Client> observableList;

    @FXML
    private Button buttonOk;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();


    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public void setServices(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setSalesFormController(SalesFormController salesFormController) {
        this.salesFormController = salesFormController;
    }

    public void onBtOkAction(ActionEvent event) throws IOException {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            sales.setClient(selectedClient);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
            salesFormController.updateTextField(sales.getClient().getName());
        }
        else {
            Alerts.showAlert("Nenhum item selecionado", null, "Selecione um item", Alert.AlertType.WARNING);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    public void updateTableView() {
        if (clientService == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Client> list = clientService.findAll();
        observableList = FXCollections.observableArrayList(list);
        clientTableView.setItems(observableList);
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
