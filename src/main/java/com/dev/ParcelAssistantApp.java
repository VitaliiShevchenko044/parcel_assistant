package com.dev;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.dev.controller.MainScreenController;
import com.dev.model.Parcel;
import com.dev.repository.ParcelRepository;
import com.dev.service.ParcelService;
import com.dev.util.BarcodeScannerManager;

import java.util.Objects;

public class ParcelAssistantApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ObservableList<Parcel> inputList = FXCollections.observableArrayList();
        ObservableList<Parcel> scannedList = FXCollections.observableArrayList();

        ParcelRepository inputRepository = new ParcelRepository(inputList);
        ParcelRepository scannedRepository = new ParcelRepository(scannedList);
        ParcelService parcelService = new ParcelService(inputRepository, scannedRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
        Parent root = loader.load();
        MainScreenController controller = loader.getController();
        controller.initWithService(parcelService);

        Scene primaryScene = new Scene(root);
        stage.setScene(primaryScene);
        primaryScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        stage.setTitle("Parcel Assistant (ver. 1.0)");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/app_icon.png")));
        stage.getIcons().add(icon);
        BarcodeScannerManager.getInstance().register(primaryScene, parcelService::processScannedParcel);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
