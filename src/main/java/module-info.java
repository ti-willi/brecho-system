module com.tiwilli.bechosystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tiwilli.bechosystem to javafx.fxml;
    exports com.tiwilli.bechosystem;
}