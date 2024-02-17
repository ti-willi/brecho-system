package com.tiwilli.bechosystem;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryListController implements Initializable {

    private CategoryService service;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, Long> tableColumnId;

    @FXML
    private TableColumn<Category, String> tableColumnName;

    @FXML
    private Button btRegister;

    private ObservableList<Category> observableList;

    @FXML
    public void onBtRegister(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Category obj = new Category();
        createDialogForm(obj, "CategoryForm.fxml", parentStage);
    }

    public void setCategoryService(CategoryService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        categoryTableView.prefHeightProperty().bind(stage.heightProperty());
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

}
