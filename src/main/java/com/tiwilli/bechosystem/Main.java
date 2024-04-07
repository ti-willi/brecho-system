package com.tiwilli.bechosystem;

import com.tiwilli.bechosystem.model.entities.*;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;
import com.tiwilli.bechosystem.model.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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

        //Category cat = new Category(1, "baba");
        //CategoryService categoryService = new CategoryService();
        //categoryService.saveOrUpdate(cat);

        //Clothes clothes = new Clothes(1, "bota", "p", 20.0, 30.0, Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), ClothesStatus.POSTED, cat);
        //ClothesService service = new ClothesService();
        //service.saveOrUpdate(clothes);

        //ClientAddress address = new ClientAddress(1, "Rio Grande do Sul", "Porto Alegre", "Auxiliadora", "Rua Eudoro Berlink", "Apto 405", 495, "90450-030");
        //ClientAddressService addressService = new ClientAddressService();
        //addressService.saveOrUpdate(address);

        /*Client client = new Client(null, "Maria", "999997777", "maria@gmail.com", address);
        ClientService clientService = new ClientService();
        clientService.saveOrUpdate(client);*/

    }
}