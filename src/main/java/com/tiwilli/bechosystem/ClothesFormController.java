package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.gui.exceptions.ValidationException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Constraints;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;
import com.tiwilli.bechosystem.model.services.CategoryService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClothesFormController implements Initializable {

    private Clothes entity;

    private ClothesService service;

    private CategoryService categoryService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSize;

    @FXML
    private TextField txtPurchaseValue;

    @FXML
    private TextField txtSalesValue;

    @FXML
    private DatePicker datePickerPurchase;

    @FXML
    private ComboBox<Category> comboBoxCategory;

    @FXML
    private Label labelSalesDate;

    @FXML
    private Label labelPostDate;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorSize;

    @FXML
    private Label labelErrorCategory;

    @FXML
    private Label labelId;

    @FXML
    private Label labelIdValue;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Category> obsList;

    private ObservableList<String> obsListComboBox;

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public void setClothes(Clothes entity) {
        this.entity = entity;
    }

    public void setServices(ClothesService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }

    @FXML
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
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Clothes getFormData() {
        Clothes obj = new Clothes();

        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(labelIdValue.getText()));

        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            exception.addError("name", "Campo requerido");
        }
        obj.setName(txtName.getText());

        if (txtSize.getText() == null || txtSize.getText().trim().isEmpty()) {
            exception.addError("size", "Campo requerido");
        }
        obj.setSize(txtSize.getText());

        obj.setPurchaseValue(Utils.tryParseToDouble(txtPurchaseValue.getText()));
        obj.setSalesValue(Utils.tryParseToDouble(txtSalesValue.getText()));

        if (datePickerPurchase.getValue() == null) {
            obj.setPurchaseDate(null);
        }
        else {
            Instant instantPurchase = Instant.from(datePickerPurchase.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setPurchaseDate(Date.from(instantPurchase));
        }

        if (labelSalesDate.getText() == null || labelSalesDate.getText().trim().isEmpty()) {
            obj.setSalesDate(null);
        }
        else {
            String instantSale = labelSalesDate.getText();
            obj.setSalesDate(Date.from(Instant.parse(instantSale)));
        }

        if (labelPostDate.getText() == null || labelPostDate.getText().trim().isEmpty()) {
            obj.setSalesDate(null);
        }
        else {
            String instantPost = labelPostDate.getText();
            obj.setPostDate(Date.from(Instant.parse(instantPost)));
        }

        if (labelStatus.getText() == null || labelStatus.getText().trim().isEmpty()) {
            obj.setStatus(null);
        }
        obj.setStatus(ClothesStatus.valueOf(labelStatus.getText()));

        if (comboBoxCategory.getValue() == null) {
            exception.addError("category", "Selecione uma categoria");
        }
        obj.setCategory(comboBoxCategory.getValue());

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldMaxLength(txtSize, 5);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Utils.formatDatePicker(datePickerPurchase, "dd/MM/yyyy");
        initializeCategoryComboBox();
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (entity.getId() != null) {
            labelId.setText("Código");
            labelIdValue.setText(String.valueOf(entity.getId()));
        }

        Locale.setDefault(Locale.US);

        txtName.setText(entity.getName());
        txtSize.setText(entity.getSize());
        txtPurchaseValue.setText(String.format("%.2f", entity.getPurchaseValue()));
        txtSalesValue.setText(String.format("%.2f", entity.getSalesValue()));

        if (entity.getPurchaseDate() != null) {
            datePickerPurchase.setValue(LocalDate.ofInstant(entity.getPurchaseDate().toInstant(), ZoneId.systemDefault()));
        }
        else {
            Utils.formatDatePicker(datePickerPurchase, "dd/MM/yyyy");
        }

        labelSalesDate.setText(Utils.formatLabelDate(entity.getSalesDate(), "dd/MM/yyyy"));
        labelPostDate.setText(Utils.formatLabelDate(entity.getPostDate(), "dd/MM/yyyy"));

        if (entity.getStatus() == null) {
            entity.setStatus(ClothesStatus.NOT_POSTED);
        }
        labelStatus.setText(entity.getStatus().getDescription());

        comboBoxCategory.setValue(entity.getCategory());
    }

    public void loadAssociatedObjects() {
        if (categoryService == null) {
            throw new IllegalStateException("CategoryService was null");
        }

        List<Category> list = categoryService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxCategory.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
        labelErrorSize.setText(fields.contains("size") ? errors.get("size") : "");
        labelErrorCategory.setText(fields.contains("category") ? errors.get("category") : "");
    }

    private void initializeCategoryComboBox() {
        Callback<ListView<Category>, ListCell<Category>> factory = lv -> new ListCell<Category>() {

            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        comboBoxCategory.setCellFactory(factory);
        comboBoxCategory.setButtonCell(factory.call(null));
    }

}
