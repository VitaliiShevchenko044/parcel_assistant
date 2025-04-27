module ParcelAssistant {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens ua.shevchenko.controller to javafx.fxml;
    exports ua.shevchenko;
    exports ua.shevchenko.model;
}