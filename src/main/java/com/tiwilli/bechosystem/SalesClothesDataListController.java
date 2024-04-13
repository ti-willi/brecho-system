package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SalesClothesDataListController implements Initializable {

    @FXML
    private TableView<Clothes> clothesTableView;

    @FXML
    private TableColumn<Clothes, String> tableColumnName;

    @FXML
    private TableColumn<Clothes, String> tableColumnSize;

    @FXML
    private TableColumn<Clothes, String> tableColumnCategoryName;

    @FXML
    private TableColumn<Clothes, String> tableColumnStatus;

    @FXML
    private Label labelId;

    @FXML
    private Label labelName;

    @FXML
    private Label labelSize;

    @FXML
    private Label labelCategory;

    @FXML
    private Label labelPurchaseValue;

    @FXML
    private Label labelSalesValue;

    @FXML
    private Label labelPurchaseDate;

    @FXML
    private Label labelSalesDate;

    @FXML
    private Label labelPostDate;

    @FXML
    private Label labelStatus;

    private Sales entity;

    private ClothesService service;

    private ObservableList<Clothes> observableList;


    public void setSales(Sales entity) {
        this.entity = entity;
    }

    public void setService(ClothesService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        showStatusDescription();
        showCategoryName();

        clothesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectItemsClothesTableView(newValue);
                }
        );
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        List<Clothes> clothesList = service.findBySales(entity);
        loadTableViewProducts(clothesList);
    }

    private void loadTableViewProducts(List<Clothes> clothesList) {
        observableList = FXCollections.observableArrayList(clothesList);
        clothesTableView.setItems(observableList);
    }

    private void selectItemsClothesTableView(Clothes obj) {
        Locale.setDefault(Locale.US);

        if (obj != null) {
            labelId.setText(String.valueOf(obj.getId()));
            labelName.setText(obj.getName());
            labelSize.setText(obj.getSize());
            labelCategory.setText(obj.getCategory().getName());
            labelPurchaseValue.setText(String.format("%.2f", obj.getPurchaseValue()));
            labelSalesValue.setText(String.format("%.2f", obj.getSalesValue()));
            labelPurchaseDate.setText(Utils.formatLabelDate(obj.getPurchaseDate(), "dd/MM/yyyy"));
            labelSalesDate.setText(Utils.formatLabelDate(obj.getSalesDate(), "dd/MM/yyyy"));
            labelPostDate.setText(Utils.formatLabelDate(obj.getPostDate(), "dd/MM/yyyy"));
            labelStatus.setText(obj.getStatus().getDescription());
        }
        else {
            return;
        }
    }

    private void showStatusDescription() {
        tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getDescription()));
        tableColumnStatus.setCellFactory(column -> new TableCell<Clothes, String>() {
            @Override
            protected void updateItem(String statusDescription, boolean empty) {
                super.updateItem(statusDescription, empty);

                if (statusDescription == null) {
                    setText(null);
                }
                else {
                    setText(statusDescription);
                }
            }
        });
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
}
