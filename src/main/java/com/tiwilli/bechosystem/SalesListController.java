package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import com.tiwilli.bechosystem.model.services.SalesService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SalesListController implements Initializable, DataChangeListener {

    private SalesService service;

    private ClientService clientService;

    private ClothesService clothesService;

    @FXML
    private TableView<Sales> salesTableView;

    @FXML
    private TableColumn<Sales, String> tableColumnClientName;

    @FXML
    private TableColumn<Sales, Integer> tableColumnQuantity;

    @FXML
    private TableColumn<Sales, Date> tableColumnSalesDate;

    @FXML
    private TableColumn<Sales, Double> tableColumnTotalAmount;

    @FXML
    private Label labelId;

    @FXML
    private Label labelSalesDate;

    @FXML
    private Label labelTotalAmount;

    @FXML
    private Label labelProfit;

    @FXML
    private Button btClient;

    @FXML
    private Button btProduct;

    @FXML
    private Button btRegister;

    @FXML
    private Button btEdit;

    @FXML
    private Button btDelete;

    private ObservableList<Sales> observableList;

    public void setServices(SalesService service, ClothesService clothesService) {
        this.service = service;
        this.clothesService = clothesService;
    }

    @FXML
    public void obBtClientAction(ActionEvent event) {
        System.out.println("btClient");
    }

    @FXML
    public void onBtProductAction(ActionEvent event) {
        System.out.println("btProduct");
    }

    @FXML
    public void onBtRegisterAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Sales obj = new Sales();
        createDialogForm(obj, "SalesForm.fxml", parentStage);
    }

    @FXML
    public void onBtEditAction(ActionEvent event) {
        Sales obj = salesTableView.getSelectionModel().getSelectedItem();

        if (obj != null) {
            Stage parentStage = Utils.currentStage(event);
            createDialogForm(obj,"SalesForm.fxml", parentStage);
            salesTableView.refresh();
        }
        else {
            Alerts.showAlert("Nenhum salese selecionado", null, "Selecione um sales para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onBtDeleteAction() {
        Sales obj = salesTableView.getSelectionModel().getSelectedItem();

        if (obj != null) {
            Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");

            if (result.get() == ButtonType.OK) {
                if (service == null) {
                    throw new IllegalStateException("Service was null");
                }
                if (clientService == null) {
                    throw new IllegalStateException("ClientService was null");
                }
                if (clothesService == null) {
                    throw new IllegalStateException("ClothesService was null");
                }
                try {
                    service.remove(obj);
                    updateTableView();
                    clearItemsSalesTableView();
                }
                catch (DbIntegrityException e) {
                    Alerts.showAlert("Erro!", null, "Você não pode remover uma categoria associada a um produto", Alert.AlertType.ERROR);
                }
            }
        }
        else {
            Alerts.showAlert("Nenhum salese selecionado", null, "Selecione um salese para excluir", Alert.AlertType.WARNING);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        showClientNameColumn();
        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnSalesDate.setCellValueFactory(new PropertyValueFactory<>("salesDate"));
        tableColumnTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        Stage stage = (Stage) Main.getMainScene().getWindow();
        salesTableView.prefHeightProperty().bind(stage.heightProperty());

        salesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectItemsSalesTableView(newValue);
                }
        );
    }

    private void selectItemsSalesTableView(Sales obj) {

        Locale.setDefault(Locale.US);

        if (obj != null) {
            labelId.setText(String.valueOf(obj.getId()));
            labelSalesDate.setText(Utils.formatLabelDate(obj.getSalesDate(), "dd/MM/yyyy"));
            labelTotalAmount.setText(String.valueOf(obj.getTotalAmount()));
            labelProfit.setText(String.valueOf(obj.getProfit()));
        }
        else {
            return;
        }
    }

    private void clearItemsSalesTableView() {
        labelId.setText("");
        labelSalesDate.setText("");
        labelTotalAmount.setText("");
        labelProfit.setText("");
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Sales> list = service.findAll();
        observableList = FXCollections.observableArrayList(list);
        salesTableView.setItems(observableList);
    }

    private void createDialogForm(Sales obj, String absoluteName, Stage parentStage) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
                Pane pane = loader.load();

                SalesFormController controller = loader.getController();
                controller.setSales(obj);
                controller.setServices(new SalesService(), new ClothesService());
                controller.setSalesListController(this);
                controller.subscribeDataChangeListener(this);
                controller.updateFormData();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Entre com os dados do salese");
                dialogStage.setScene(new Scene(pane));
                dialogStage.setResizable(false);
                dialogStage.initOwner(parentStage);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.showAndWait();
            }
            catch (IOException e) {
                e.printStackTrace();
                Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
            }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void showClientNameColumn() {
        tableColumnClientName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getName()));
        tableColumnClientName.setCellFactory(column -> new TableCell<Sales, String>() {
            @Override
            protected void updateItem(String clientName, boolean empty) {
                super.updateItem(clientName, empty);

                setText(clientName);
            }
        });
    }

}
