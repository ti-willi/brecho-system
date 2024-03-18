package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.services.ClientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesClientListController implements Initializable, DataChangeListener {

    private Client client;

    private ClientService clientService;

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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void onBtOkAction(ActionEvent event) {
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

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
