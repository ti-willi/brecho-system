module com.tiwilli.bechosystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.tiwilli.bechosystem to javafx.fxml;
    exports com.tiwilli.bechosystem;
    exports com.tiwilli.bechosystem.model.entities;
    exports com.tiwilli.bechosystem.model.entities.enums;
    exports com.tiwilli.bechosystem.model.services;
    exports com.tiwilli.bechosystem.gui.listeners;
    exports com.tiwilli.bechosystem.gui.util;
}