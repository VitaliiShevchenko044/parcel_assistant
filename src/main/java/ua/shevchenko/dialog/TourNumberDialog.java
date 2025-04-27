package ua.shevchenko.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shevchenko.controller.TourNumberDialogController;
import ua.shevchenko.service.ParcelService;
import ua.shevchenko.util.BarcodeScannerManager;

import java.io.IOException;
import java.util.Objects;

public class TourNumberDialog {

    private static final Logger log = LoggerFactory.getLogger(TourNumberDialog.class);

    private static final TourNumberDialog INSTANCE = new TourNumberDialog();
    private Stage stage;
    private TourNumberDialogController controller;
    private final Image ICON = new Image(
            Objects.requireNonNull(TourNumberDialog.class.getResourceAsStream("/images/icons/app_icon.png")));

    private TourNumberDialog() {}

    public static TourNumberDialog getInstance() {return INSTANCE;}

    public void showDialog(ParcelService parcelService, String tourNumber) {
        if (stage != null && stage.isShowing()) {
            controller.setTourNumberLabel(tourNumber);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TourNumberDialog.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setTourNumberLabel(tourNumber);

            stage = new Stage();
            stage.setTitle("Tour Number");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(ICON);
            BarcodeScannerManager barcodeScannerManager = BarcodeScannerManager.getInstance();
            barcodeScannerManager.register(scene, parcelService::processScannedParcel);
            stage.setOnHidden(event -> {
                stage = null;
                controller = null;
            });
            stage.show();

        } catch (IOException e) {
            log.error("Error displaying window for showing tour number");
        }
    }
}
