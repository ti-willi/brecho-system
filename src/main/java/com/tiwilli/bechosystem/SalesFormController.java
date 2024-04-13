package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.exceptions.ValidationException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import com.tiwilli.bechosystem.model.services.SalesService;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SalesFormController implements Initializable, DataChangeListener {

    private Sales entity;

    private SalesService service;

    private ClothesService clothesService;

    private SalesListController salesListController;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtClientName;

    @FXML
    private DatePicker dpSalesDate;

    @FXML
    private Label labelId;

    @FXML
    private Label labelIdValue;

    @FXML
    private Label labelErrorClientName;

    @FXML
    private TableView<Clothes> tableViewProducts;

    @FXML
    private TableColumn<Clothes, String> tableColumnProductName;

    @FXML
    private TableColumn<Clothes, String> tableColumnSize;

    @FXML
    private TableColumn<Clothes, String> tableColumnCategoryName;

    @FXML
    private TableColumn<Clothes, Double> tableColumnPurchaseValue;

    @FXML
    private TableColumn<Clothes, Double> tableColumnSalesValue;

    @FXML
    private Button btFindClient;

    @FXML
    private Button btAddProduct;

    @FXML
    private Button btRemoveProduct;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Clothes> observableList;


    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public void setSales(Sales entity) {
        this.entity = entity;
    }

    public void setServices(SalesService service, ClothesService clothesService) {
        this.service = service;
        this.clothesService = clothesService;
    }

    public void setSalesListController(SalesListController salesListController) {
        this.salesListController = salesListController;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        if (clothesService == null) {
            throw new IllegalStateException("ClothesService was null");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);

            for (Clothes item : entity.getClothes()) {
                item.setSales(entity);
                item.setStatus(ClothesStatus.SOLD);
                item.setSalesDate(entity.getSalesDate());
                clothesService.saveOrUpdate(item);
            }

            notifyDataChangeListeners();
            salesListController.updateTableView();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro!", null, "Erro ao salvar o objeto", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @FXML
    public void onBtFindClientAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Sales obj = new Sales();
        createClientDialogForm(obj, "SalesClientList.fxml", parentStage);
    }

    @FXML
    public void onBtAddProductAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Sales obj = new Sales();
        createProductDialogForm(obj, "SalesProductList.fxml", parentStage);
    }

    @FXML
    public void onBtRemoveProductAction() {
        Clothes obj = tableViewProducts.getSelectionModel().getSelectedItem();
        if (obj != null) {
            Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");

            if (result.get() == ButtonType.OK) {
                if (service == null) {
                    throw new IllegalStateException("Service was null");
                }
                if (clothesService == null) {
                    throw new IllegalStateException("ClothesService was null");
                }
                try {
                    entity.getClothes().remove(obj);
                    obj.setStatus(ClothesStatus.AVAILABLE);
                    obj.setSales(null);
                    clothesService.saveOrUpdate(obj);
                    tableViewProducts.getItems().remove(obj);
                }
                catch (DbIntegrityException e) {
                    Alerts.showAlert("Erro!", null, "Você não pode remover uma venda com produtos cadastrados! Remova os produtos para desfazer a venda", Alert.AlertType.ERROR);
                }
            }
        }
        else {
            Alerts.showAlert("Nenhum salese selecionado", null, "Selecione um salese para excluir", Alert.AlertType.WARNING);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Sales getFormData() {
        Sales obj = new Sales();
        Client client = entity.getClient();
        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(labelIdValue.getText()));

        if (txtClientName.getText() == null || txtClientName.getText().trim().isEmpty()) {
            exception.addError("name", "Campo requerido");
        }
        obj.setClient(client);

        for (Clothes item : entity.getClothes()) {
            obj.getClothes().add(item);
        }

        obj.setQuantity(service.setQuantity(entity.getClothes()));
        obj.setTotalAmount(service.setTotalAmount(entity.getClothes()));
        obj.setProfit(service.setProfit(entity.getClothes()));

        if (txtClientName.getText() == null || txtClientName.getText().trim().isEmpty()) {
            exception.addError("name", "Campo requerido");
        }

        if (dpSalesDate.getValue() == null) {
            obj.setSalesDate(null);
        }
        else {
            Instant instantSales = Instant.from(dpSalesDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setSalesDate(Date.from(instantSales));
        }

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        return obj;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       initializeNodes();
    }

    private void initializeNodes() {
        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        showCategoryName();
        tableColumnPurchaseValue.setCellValueFactory(new PropertyValueFactory<>("purchaseValue"));
        tableColumnSalesValue.setCellValueFactory(new PropertyValueFactory<>("salesValue"));

    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (entity.getId() != null) {
            labelId.setText("Código");
            labelIdValue.setText(String.valueOf(entity.getId()));
        }

        if (entity.getClient() != null) {
            updateClient(entity.getClient());
        }

        if (entity.getSalesDate() != null) {
            dpSalesDate.setValue(LocalDate.ofInstant(entity.getSalesDate().toInstant(), ZoneId.systemDefault()));
        }
        else {
            Utils.formatDatePicker(dpSalesDate, "dd/MM/yyyy");
        }

        List<Clothes> clothesList = clothesService.findBySales(entity);
        loadTableViewProducts(clothesList);

    }

    public void updateProductTableView(Clothes clothes) {
        if (observableList.contains(clothes)) {
            Alerts.showAlert("Item já adicionado", null, "Este item já está na lista!", Alert.AlertType.INFORMATION);
        }
        else {
            entity.getClothes().add(clothes);
            tableViewProducts.getItems().add(clothes);

        }
    }

    private void loadTableViewProducts(List<Clothes> clothesList) {
        entity.getClothes().addAll(clothesList);
        observableList = FXCollections.observableArrayList(clothesList);
        tableViewProducts.setItems(observableList);
    }

    private void createClientDialogForm(Sales obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SalesClientListController controller = loader.getController();
            controller.setSales(obj);
            controller.setServices(new ClientService());
            controller.setSalesFormController(this);
            controller.subscribeDataChangeListener(this);
            controller.updateTableView();

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

    private void createProductDialogForm(Sales obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SalesProductListController controller = loader.getController();
            controller.setSales(obj);
            controller.setServices(new ClothesService());
            controller.setSalesFormController(this);
            controller.subscribeDataChangeListener(this);
            controller.updateTableView();

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
    }

    public void updateClient(Client client) {
        entity.setClient(client);
        txtClientName.setText(client.getName());

    }

    private void showCategoryName() {
        tableColumnCategoryName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        tableColumnCategoryName.setCellFactory(column -> new TableCell<Clothes, String>() {
            @Override
            protected void updateItem(String categoryName, boolean empty) {
                super.updateItem(categoryName, empty);

                if (categoryName == null) {
                    setText(null);
                }
                else {
                    setText(categoryName);
                }
            }
        });
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorClientName.setText(fields.contains("name") ? errors.get("name") : "");

    }

}
