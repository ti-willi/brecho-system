package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import com.tiwilli.bechosystem.model.services.SalesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesProductListController implements Initializable, DataChangeListener {

    private Sales sales;

    private ClothesService clothesService;

    private SalesFormController salesFormController;

    @FXML
    public TableView<Clothes> clothesTableView;

    @FXML
    public TableColumn<Clothes, String> tableColumnId;

    @FXML
    public TableColumn<Clothes, String> tableColumnName;

    @FXML
    public TableColumn<Clothes, String> tableColumnSize;

    @FXML
    public Button btOk;

    private ObservableList<Clothes> observableList;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public void setServices(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    public void setSalesFormController(SalesFormController salesFormController) {
        this.salesFormController = salesFormController;
    }

    public void onBtOkAction(ActionEvent event) {
        Clothes selectedClothes = clothesTableView.getSelectionModel().getSelectedItem();
        if (selectedClothes != null) {
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
            salesFormController.updateProductTableView(selectedClothes);
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
