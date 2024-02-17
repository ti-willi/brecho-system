package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.services.CategoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryFormController implements Initializable {

    private Category entity;

    private CategoryService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setCategory(Category entity) {
        this.entity = entity;
    }

    public void setCategoryService(CategoryService service) {
        this.service = service;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
