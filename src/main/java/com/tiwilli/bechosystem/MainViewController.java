package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.util.Alerts;
import com.tiwilli.bechosystem.model.services.CategoryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemClothes;

    @FXML
    private MenuItem menuItemCategory;

    @FXML
    private MenuItem menuItemClient;

    @FXML
    private MenuItem menuItemSale;

    @FXML
    private MenuItem menuItemShoppingBag;

    @FXML
    private MenuItem menuItemPost;

    /*@FXML
    public void onMenuItemClothesAction() {
        loadView("ClothesList.fxml");
    }*/

    @FXML
    public void onMenuItemCategoryAction() {
        loadView("CategoryList.fxml", (CategoryListController controller) -> {
            controller.setCategoryService(new CategoryService());
            controller.updateTableView();
        });
    }

    /*@FXML
    public void onMenuItemClientAction() {
        loadView("ClientList.fxml");
    }

    @FXML
    public void onMenuItemSaleAction() {
        loadView("SaleList.fxml");
    }

    @FXML
    public void onMenuItemShoppingBagAction() {
        loadView("ShoppingBagList.fxml");
    }

    @FXML
    public void onMenuItemPostAction() {
        loadView("PostList.fxml");
    }*/

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);

        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}