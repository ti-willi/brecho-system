package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.ClientAddress;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;
import com.tiwilli.bechosystem.model.services.CategoryService;
import com.tiwilli.bechosystem.model.services.ClientAddressService;
import com.tiwilli.bechosystem.model.services.ClientService;
import com.tiwilli.bechosystem.model.services.ClothesService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class Main extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        ScrollPane scrollPane = fxmlLoader.load();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        mainScene = new Scene(scrollPane);
        //stage.setMaximized(true);
        stage.setScene(mainScene);
        stage.setTitle("Brecho System");
        stage.show();
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch();

        /*Category cat = new Category(null, "baba");
        CategoryService categoryService = new CategoryService();
        categoryService.saveOrUpdate(cat);

        Clothes clothes = new Clothes(null, "bota", "p", 20.0, 30.0, null, null, null, ClothesStatus.POSTED, cat);
        ClothesService service = new ClothesService();
        service.saveOrUpdate(clothes);*/

        /*ClientAddress address = new ClientAddress(null, "Rio Grande do Sul", "Porto Alegre", "Auxiliadora", "Rua Eudoro Berlink", "Apto 405", 495, "90450-030");
        ClientAddressService addressService = new ClientAddressService();
        addressService.saveOrUpdate(address);

        Client client = new Client(null, "Maria", "999997777", "maria@gmail.com", address);
        ClientService clientService = new ClientService();
        clientService.saveOrUpdate(client);*/

    }
}