package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.gui.listeners.DataChangeListener;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalesProductListController implements Initializable {

    private Clothes clothes;

    private ClothesService clothesService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public void setClothesService(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }
}
