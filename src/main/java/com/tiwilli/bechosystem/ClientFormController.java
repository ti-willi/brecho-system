package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.gui.exceptions.ValidationException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Constraints;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;

import com.tiwilli.bechosystem.model.entities.ClientAddress;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.services.CategoryService;
import com.tiwilli.bechosystem.model.services.ClientAddressService;
import com.tiwilli.bechosystem.model.services.ClientService;

import com.tiwilli.bechosystem.model.services.ClothesService;
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

import java.util.*;

public class ClientFormController implements Initializable {

    private Client entity;

    private ClientAddress address;

    private ClientService service;

    private ClientAddressService addressService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtZipCode;

    @FXML
    private TextField txtState;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtDistrict;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtAddressComplement;

    @FXML
    private TextField txtAddressNumber;

    @FXML
    private Label labelId;

    @FXML
    private Label labelIdValue;

    @FXML
    private Label labelAddressIdValue;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorZipCode;

    @FXML
    private Label labelErrorState;

    @FXML
    private Label labelErrorCity;

    @FXML
    private Label labelErrorDistrict;

    @FXML
    private Label labelErrorStreet;

    @FXML
    private Label labelErrorAddressComplement;

    @FXML
    private Label labelErrorAddressNumber;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public void setClient(Client entity) {
        this.entity = entity;
    }

    public void setAddress(ClientAddress address) {
        this.address = address;
    }

    public void setServices(ClientService service, ClientAddressService addressService) {
        this.service = service;
        this.addressService = addressService;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (address == null) {
            throw new IllegalStateException("Address Entity was null");

        }
        if (addressService == null) {
            throw new IllegalStateException("Address service was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        try {
            entity = getFormData();
            addressService.saveOrUpdate(entity.getAddress());
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
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Client getFormData() {
        Client obj = new Client();
        ClientAddress clientAddress = new ClientAddress();

        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(labelIdValue.getText()));
        clientAddress.setId(Utils.tryParseToInt(labelAddressIdValue.getText()));

        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            exception.addError("name", "Campo requerido");
        }
        obj.setName(txtName.getText());

        obj.setPhone(txtPhone.getText());
        obj.setEmail(txtEmail.getText());

        if (txtZipCode.getText() == null || txtZipCode.getText().trim().isEmpty()) {
            exception.addError("zipCode", "Campo requerido");
        }
        clientAddress.setZipCode(txtZipCode.getText());

        if (txtState.getText() == null || txtState.getText().trim().isEmpty()) {
            exception.addError("state", "Campo requerido");
        }
        clientAddress.setState(txtState.getText());

        if (txtCity.getText() == null || txtCity.getText().trim().isEmpty()) {
            exception.addError("city", "Campo requerido");
        }
        clientAddress.setCity(txtCity.getText());

        if (txtDistrict.getText() == null || txtDistrict.getText().trim().isEmpty()) {
            exception.addError("district", "Campo requerido");
        }
        clientAddress.setDistrict(txtDistrict.getText());

        if (txtStreet.getText() == null || txtStreet.getText().trim().isEmpty()) {
            exception.addError("street", "Campo requerido");
        }
        clientAddress.setStreet(txtStreet.getText());

        if (txtAddressComplement.getText() == null || txtAddressComplement.getText().trim().isEmpty()) {
            exception.addError("addressComplement", "Campo requerido");
        }
        clientAddress.setAddressComplement(txtAddressComplement.getText());

        if (txtAddressNumber.getText() == null || txtAddressNumber.getText().trim().isEmpty()) {
            exception.addError("number", "Campo requerido");
        }
        clientAddress.setNumber(Utils.tryParseToInt(txtAddressNumber.getText()));

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        obj.setAddress(clientAddress);
        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializeNodes();
    }

    private void initializeNodes() {
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
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (address == null) {
            throw new IllegalStateException("Address was null");
        }

        if (entity.getId() != null) {
            labelId.setText("Código");
            labelIdValue.setText(String.valueOf(entity.getId()));
        }

        if (address.getId() != null) {
            labelAddressIdValue.setText(String.valueOf(address.getId()));
            labelAddressIdValue.setVisible(false);
        }

        txtName.setText(entity.getName());
        txtPhone.setText(entity.getPhone());
        txtEmail.setText(entity.getEmail());
        txtZipCode.setText(address.getZipCode());
        txtState.setText(address.getState());
        txtCity.setText(address.getCity());
        txtDistrict.setText(address.getDistrict());
        txtStreet.setText(address.getStreet());
        txtAddressComplement.setText(address.getAddressComplement());
        txtAddressNumber.setText(String.valueOf(address.getNumber()));
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
        labelErrorZipCode.setText(fields.contains("zipCode") ? errors.get("zipCode") : "");
        labelErrorState.setText(fields.contains("state") ? errors.get("state") : "");
        labelErrorCity.setText(fields.contains("city") ? errors.get("city") : "");
        labelErrorDistrict.setText(fields.contains("district") ? errors.get("district") : "");
        labelErrorStreet.setText(fields.contains("street") ? errors.get("street") : "");
        labelErrorAddressComplement.setText(fields.contains("addressComplement") ? errors.get("addressComplement") : "");
        labelErrorAddressNumber.setText(fields.contains("number") ? errors.get("number") : "");


    }

}
