module ParcelAssistant {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens com.dev.controller to javafx.fxml;
    exports com.dev;
    exports com.dev.model;
}