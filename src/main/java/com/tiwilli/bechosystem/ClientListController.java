package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.ClientAddress;
import com.tiwilli.bechosystem.model.services.ClientAddressService;
import com.tiwilli.bechosystem.model.services.ClientService;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientListController implements Initializable, DataChangeListener {

    private ClientService service;

    private ClientAddressService addressService;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, String> tableColumnName;

    @FXML
    private TableColumn<Client, String> tableColumnPhone;

    @FXML
    private TableColumn<Client, String> tableColumnEmail;

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

    @FXML
    private Button btRegister;

    @FXML
    private Button btEdit;

    @FXML
    private Button btDelete;

    private ObservableList<Client> observableList;

    public void setClientService(ClientService service) {
        this.service = service;
    }

    public void setClientAddressService(ClientAddressService addressService) {
        this.addressService = addressService;
    }

    @FXML
    public void onBtRegisterAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        ClientAddress address = new ClientAddress();
        Client obj = new Client();
        createDialogForm(obj, address, "ClientForm.fxml", parentStage);
    }

    @FXML
    public void onBtEditAction(ActionEvent event) {
        Client obj = clientTableView.getSelectionModel().getSelectedItem();

        if (obj != null) {
            Stage parentStage = Utils.currentStage(event);
            createDialogForm(obj, obj.getAddress(), "ClientForm.fxml", parentStage);
            clientTableView.refresh();
        }
        else {
            Alerts.showAlert("Nenhum cliente selecionado", null, "Selecione um client para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onBtDeleteAction() {
        Client obj = clientTableView.getSelectionModel().getSelectedItem();

        if (obj != null) {
            Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");

            if (result.get() == ButtonType.OK) {
                if (service == null) {
                    throw new IllegalStateException("Service was null");
                }
                if (addressService == null) {
                    throw new IllegalStateException("Address service was null");
                }
                try {
                    service.remove(obj);
                    addressService.remove(obj.getAddress());
                    updateTableView();
                    clearItemsClientTableView();
                }
                catch (DbIntegrityException e) {
                    Alerts.showAlert("Erro!", null, "Você não pode remover uma categoria associada a um produto", Alert.AlertType.ERROR);
                }
            }
        }
        else {
            Alerts.showAlert("Nenhum cliente selecionado", null, "Selecione um cliente para excluir", Alert.AlertType.WARNING);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        clientTableView.prefHeightProperty().bind(stage.heightProperty());

        clientTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectItemsClientTableView(newValue);
                }
        );
    }

    private void selectItemsClientTableView(Client obj) {

        Locale.setDefault(Locale.US);

        if (obj != null) {
            labelId.setText(String.valueOf(obj.getId()));
            labelName.setText(obj.getName());
            labelPhone.setText(obj.getPhone());
            labelEmail.setText(obj.getEmail());
            labelZipCode.setText(obj.getAddress().getZipCode());
            labelState.setText(obj.getAddress().getState());
            labelCity.setText(obj.getAddress().getCity());
            labelDistrict.setText(obj.getAddress().getDistrict());
            labelStreet.setText(obj.getAddress().getStreet());
            labelAddressComplement.setText((obj.getAddress().getAddressComplement()));
            labelAddressNumber.setText(String.valueOf(obj.getAddress().getNumber()));
        }
        else {
            return;
        }
    }

    private void clearItemsClientTableView() {
        labelId.setText("");
        labelName.setText("");
        labelPhone.setText("");
        labelEmail.setText("");
        labelZipCode.setText("");
        labelState.setText("");
        labelCity.setText("");
        labelDistrict.setText("");
        labelStreet.setText("");
        labelAddressComplement.setText("");
        labelAddressNumber.setText("");
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Client> list = service.findAll();
        observableList = FXCollections.observableArrayList(list);
        clientTableView.setItems(observableList);
    }

    private void createDialogForm(Client obj, ClientAddress address, String absoluteName, Stage parentStage) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
                Pane pane = loader.load();

                ClientFormController controller = loader.getController();
                controller.setClient(obj);
                controller.setAddress(address);
                controller.setServices(new ClientService(), new ClientAddressService());
                controller.subscribeDataChangeListener(this);
                controller.updateFormData();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Entre com os dados do cliente");
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

}
