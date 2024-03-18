package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesProductListController implements Initializable, DataChangeListener {

    private Clothes clothes;

    private ClothesService clothesService;

    @FXML
    public TableView<Clothes> clothesTableView;

    @FXML
    public TableColumn<Clothes, String> tableColumnId;

    @FXML
    public TableColumn<Clothes, String> tableColumnName;

    @FXML
    public TableColumn<Clothes, String> tableColumnSize;

    private ObservableList<Clothes> observableList;

    @FXML
    public Button btOk;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public void setClothesService(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
    }

    public void updateTableView() {
        if (clothesService == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Clothes> list = clothesService.findAll();
        observableList = FXCollections.observableArrayList(list);
        clothesTableView.setItems(observableList);
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
