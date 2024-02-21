package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.services.CategoryService;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryListController implements Initializable, DataChangeListener {

    private CategoryService service;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, String> tableColumnName;

    @FXML
    private Label labelId;

    @FXML
    private Label labelName;

    @FXML
    private Button btRegister;

    @FXML
    private Button btEdit;

    @FXML
    private Button btDelete;

    private ObservableList<Category> observableList;

    public void setCategoryService(CategoryService service) {
        this.service = service;
    }

    @FXML
    public void onBtRegisterAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Category obj = new Category();
        createDialogForm(obj, "CategoryForm.fxml", parentStage);
    }

    @FXML
    public void onBtEditAction(ActionEvent event) {
        Category obj = categoryTableView.getSelectionModel().getSelectedItem();
        if (obj != null) {
            Stage parentStage = Utils.currentStage(event);
            obj = service.findById(obj.getId());
            createDialogForm(obj, "CategoryForm.fxml", parentStage);
            categoryTableView.refresh();
        }
        else {
            Alerts.showAlert("Nenhuma categoria selecionada", null, "Selecione uma categoria para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onBtDeleteAction() {
        Category obj = categoryTableView.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            }
            catch (DbIntegrityException e) {
                Alerts.showAlert("Erro!", null, "Você não pode remover uma categoria associada a um produto", Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        categoryTableView.prefHeightProperty().bind(stage.heightProperty());

        categoryTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectItemCategoryTableView(newValue);
                }
        );
    }

    private void selectItemCategoryTableView(Category obj) {
        if (obj != null) {
            labelId.setText(String.valueOf(obj.getId()));
            labelName.setText(obj.getName());
        }
        else {
            return;
        }
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Category> list = service.findAll();
        observableList = FXCollections.observableArrayList(list);
        categoryTableView.setItems(observableList);
    }

    private void createDialogForm(Category obj, String absoluteName, Stage parentStage) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
                Pane pane = loader.load();

                CategoryFormController controller = loader.getController();
                controller.setCategory(obj);
                controller.setCategoryService(new CategoryService());
                controller.subscribeDataChangeListener(this);
                controller.updateFormData();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Entre com os dados da categoria");
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
