package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.services.ClientService;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesClientListController implements Initializable {

    private Client client;

    private ClientService clientService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setClient(Client client) {
        this.client = client;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }
}
