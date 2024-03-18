package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.gui.exceptions.ValidationException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Constraints;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SalesFormController implements Initializable, DataChangeListener {

    private Sales entity;

    private SalesService service;

    private Client client;

    private ClientService clientService;

    private Clothes clothes;

    private ClothesService clothesService;

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

    public void setServices(SalesService service, ClientService clientService, ClothesService clothesService) {
        this.service = service;
        this.clientService = clientService;
        this.clothesService = clothesService;
    }

    /*@FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro!", null, "Erro ao salvar o objeto", Alert.AlertType.ERROR);
        }
    }*/

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    /*private Sales getFormData() {
        Sales obj = new Sales();
        Client client = new Client();
        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(labelIdValue.getText()));

        if (txtClientName.getText() == null || txtClientName.getText().trim().isEmpty()) {
            exception.addError("name", "Campo requerido");
        }
        obj.setClient();

        obj.setPhone(txtPhone.getText());
        obj.setEmail(txtEmail.getText());

        if (txtZipCode.getText() == null || txtZipCode.getText().trim().isEmpty()) {
            exception.addError("zipCode", "Campo requerido");
        }
        SalesAddress.setZipCode(txtZipCode.getText());

        if (txtState.getText() == null || txtState.getText().trim().isEmpty()) {
            exception.addError("state", "Campo requerido");
        }
        SalesAddress.setState(txtState.getText());

        if (txtCity.getText() == null || txtCity.getText().trim().isEmpty()) {
            exception.addError("city", "Campo requerido");
        }
        SalesAddress.setCity(txtCity.getText());

        if (txtDistrict.getText() == null || txtDistrict.getText().trim().isEmpty()) {
            exception.addError("district", "Campo requerido");
        }
        SalesAddress.setDistrict(txtDistrict.getText());

        if (txtStreet.getText() == null || txtStreet.getText().trim().isEmpty()) {
            exception.addError("street", "Campo requerido");
        }
        SalesAddress.setStreet(txtStreet.getText());

        if (txtAddressComplement.getText() == null || txtAddressComplement.getText().trim().isEmpty()) {
            exception.addError("addressComplement", "Campo requerido");
        }
        SalesAddress.setAddressComplement(txtAddressComplement.getText());

        if (txtAddressNumber.getText() == null || txtAddressNumber.getText().trim().isEmpty()) {
            exception.addError("number", "Campo requerido");
        }
        SalesAddress.setNumber(Utils.tryParseToInt(txtAddressNumber.getText()));

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        obj.setAddress(SalesAddress);
        return obj;
    }*/

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @FXML
    public void onBtFindClientAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Client obj = new Client();
        createClientDialogForm(obj, "SalesClientList.fxml", parentStage);
    }

    @FXML
    public void onBtAddProductAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Clothes obj = new Clothes();
        createProductDialogForm(obj, "SalesProductList.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // initializeNodes();
    }

    /*private void initializeNodes() {
        Constraints.setTextFieldMaxLength(txtName, 40);
        Constraints.setTextFieldMaxLength(txtPhone, 20);
        Constraints.setTextFieldMaxLength(txtEmail, 40);
        Constraints.setTextFieldMaxLength(txtZipCode, 10);
        Constraints.setTextFieldMaxLength(txtState, 40);
        Constraints.setTextFieldMaxLength(txtCity, 40);
        Constraints.setTextFieldMaxLength(txtDistrict, 30);
        Constraints.setTextFieldMaxLength(txtStreet, 40);
        Constraints.setTextFieldMaxLength(txtAddressComplement, 20);
        Constraints.setTextFieldInteger(txtAddressNumber);
    }*/

    /*public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (entity.getId() != null) {
            labelId.setText("Código");
            labelIdValue.setText(String.valueOf(entity.getId()));
        }

        txtClientName.setText(entity.getClient().getName());

        if (entity.getSalesDate() != null) {
            dpSalesDate.setValue(LocalDate.ofInstant(entity.getSalesDate().toInstant(), ZoneId.systemDefault()));
        }
        else {
            Utils.formatDatePicker(dpSalesDate, "dd/MM/yyyy");
        }
    }

    public void updateTableView() {
        if (clothesService == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Clothes> list = clothesService.findAll();
        observableList = FXCollections.observableArrayList(list);
        tableViewProducts.setItems(observableList);
    }*/

    private void createClientDialogForm(Client obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SalesClientListController controller = loader.getController();
            controller.setClient(obj);
            controller.setClientService(new ClientService());
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

    private void createProductDialogForm(Clothes obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SalesProductListController controller = loader.getController();
            controller.setClothes(obj);
            controller.setClothesService(new ClothesService());
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


    /*private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
        labelErrorZipCode.setText(fields.contains("zipCode") ? errors.get("zipCode") : "");
        labelErrorState.setText(fields.contains("state") ? errors.get("state") : "");
        labelErrorCity.setText(fields.contains("city") ? errors.get("city") : "");
        labelErrorDistrict.setText(fields.contains("district") ? errors.get("district") : "");
        labelErrorStreet.setText(fields.contains("street") ? errors.get("street") : "");
        labelErrorAddressComplement.setText(fields.contains("addressComplement") ? errors.get("addressComplement") : "");
        labelErrorAddressNumber.setText(fields.contains("number") ? errors.get("number") : "");

    }*/

}
