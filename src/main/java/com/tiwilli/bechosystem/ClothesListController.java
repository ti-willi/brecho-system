package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.services.CategoryService;
import com.tiwilli.bechosystem.model.services.ClothesService;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClothesListController implements Initializable, DataChangeListener {

    private ClothesService service;

    private CategoryService categoryService;

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

    @FXML
    private Button btRegister;

    @FXML
    private Button btEdit;

    @FXML
    private Button btDelete;

    private ObservableList<Clothes> observableList;

    public void setClothesService(ClothesService service) {
        this.service = service;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void onBtRegisterAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Clothes obj = new Clothes();
        createDialogForm(obj, "ClothesForm.fxml", parentStage);
    }

    @FXML
    public void onBtEditAction(ActionEvent event) {
        Clothes obj = clothesTableView.getSelectionModel().getSelectedItem();
        if (obj != null) {
            Stage parentStage = Utils.currentStage(event);
            createDialogForm(obj, "ClothesForm.fxml", parentStage);
            clothesTableView.refresh();
        }
        else {
            Alerts.showAlert("Nenhuma peça selecionada", null, "Selecione uma peça para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onBtDeleteAction() {
        Clothes obj = clothesTableView.getSelectionModel().getSelectedItem();

        if (obj != null) {
            Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");

            if (result.get() == ButtonType.OK) {
                if (service == null) {
                    throw new IllegalStateException("Service was null");
                }
                try {
                    service.remove(obj);
                    updateTableView();
                    clearItemsClothesTableView();
                }
                catch (DbIntegrityException e) {
                    Alerts.showAlert("Erro!", null, "Você não pode remover um produto associado a uma venda", Alert.AlertType.ERROR);
                }
            }
        }
        else {
            Alerts.showAlert("Nenhuma peça selecionada", null, "Selecione uma peça para excluir", Alert.AlertType.WARNING);
        }
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

        Stage stage = (Stage) Main.getMainScene().getWindow();
        clothesTableView.prefHeightProperty().bind(stage.heightProperty());

        clothesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectItemsClothesTableView(newValue);
                }
        );
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

    private void clearItemsClothesTableView() {
        labelId.setText("");
        labelName.setText("");
        labelSize.setText("");
        labelPurchaseValue.setText("");
        labelSalesValue.setText("");
        labelPurchaseDate.setText("");
        labelSalesDate.setText("");
        labelPostDate.setText("");
        labelStatus.setText("");
        labelCategory.setText("");
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Clothes> list = service.findAll();
        observableList = FXCollections.observableArrayList(list);
        clothesTableView.setItems(observableList);
    }

    private void createDialogForm(Clothes obj, String absoluteName, Stage parentStage) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
                Pane pane = loader.load();

                ClothesFormController controller = loader.getController();
                controller.setClothes(obj);
                controller.setServices(new ClothesService(), new CategoryService());
                controller.loadAssociatedObjects();
                controller.subscribeDataChangeListener(this);
                controller.updateFormData();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Entre com os dados da peça");
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
