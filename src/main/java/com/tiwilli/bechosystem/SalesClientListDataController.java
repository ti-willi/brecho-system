package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.ClientAddress;
import com.tiwilli.bechosystem.model.services.ClientAddressService;
import com.tiwilli.bechosystem.model.services.ClientService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SalesClientListDataController implements Initializable {

    @FXML
    private Label labelId;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelEmail;

    @FXML
    private Label labelZipCode;

    @FXML
    private Label labelState;

    @FXML
    private Label labelCity;

    @FXML
    private Label labelDistrict;

    @FXML
    private Label labelStreet;

    @FXML
    private Label labelAddressComplement;

    @FXML
    private Label labelAddressNumber;

    private Client entity;

    private ClientService service;

    public void setClient(Client entity) {
        this.entity = entity;
    }

    public void setService(ClientService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void updateFormData() {
        entity = service.findById(entity.getId());

        labelId.setText(entity.getId().toString());
        labelName.setText(entity.getName());
        labelPhone.setText(entity.getPhone());
        labelEmail.setText(entity.getEmail());
        labelZipCode.setText(entity.getAddress().getZipCode());
        labelState.setText(entity.getAddress().getState());
        labelCity.setText(entity.getAddress().getCity());
        labelDistrict.setText(entity.getAddress().getDistrict());
        labelStreet.setText(entity.getAddress().getStreet());
        labelAddressComplement.setText(entity.getAddress().getAddressComplement());
        labelAddressNumber.setText(entity.getAddress().getNumber());
    }
}
