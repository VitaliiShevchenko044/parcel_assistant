package ua.shevchenko.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shevchenko.dialog.TourNumberDialog;
import ua.shevchenko.io.writer.TXTWriter;
import ua.shevchenko.model.Parcel;
import ua.shevchenko.service.ParcelFileLoaderService;
import ua.shevchenko.service.ParcelService;
import ua.shevchenko.util.ClipboardUtils;
import ua.shevchenko.util.LofFileOpener;

import java.util.Optional;

public class MainScreenController {

    private static final Logger log = LoggerFactory.getLogger(MainScreenController.class);
    private ParcelService parcelService;

    @FXML private TableView<Parcel> inputParcelsTable;
    @FXML private TableColumn<Parcel,Integer> inputIndexCol;
    @FXML private TableColumn<Parcel, String> inputTrackingCol;
    @FXML private TableColumn<Parcel, String> inputGibitCol;
    @FXML private TableColumn<Parcel, String> inputToutCol;

    @FXML private TableView<Parcel> scannedParcelsTable;
    @FXML private TableColumn<Parcel,Integer> scannedIndexCol;
    @FXML private TableColumn<Parcel, String> scannedTrackingCol;
    @FXML private TableColumn<Parcel, String> scannedGibitCol;
    @FXML private TableColumn<Parcel, String> scannedToutCol;

    @FXML private TextField manualInputField;
    @FXML private TextField manualScanField;
    @FXML private Label description;

    public void initWithService(ParcelService parcelService) {
        this.parcelService = parcelService;
        ClipboardUtils.setupCopyHandler(inputParcelsTable, scannedParcelsTable);
        setupTable(parcelService.getInputList(), inputParcelsTable, inputIndexCol, inputTrackingCol, inputGibitCol, inputToutCol);
        setupTable(parcelService.getScannedList(), scannedParcelsTable, scannedIndexCol, scannedTrackingCol, scannedGibitCol, scannedToutCol);
        parcelService.setRescanListener(this::showDialogOnRescan);
        addParcelToInputListManually();
        addParcelToScannedListManually();
    }

    private void addParcelToInputListManually() {
        manualInputField.setOnAction(event -> {
            String inputLine = manualInputField.getText().trim();
            String[] parts = inputLine.split("\\s+");
            if (parts.length == 3 && parts[1].length() == 5 && parts[2].length() == 3) {
                Parcel parcel = new Parcel(parts[0], parts[1], parts[2]);
                parcelService.addParcelToInput(parcel);
            }
            manualInputField.clear();
        });
    }

    private void addParcelToScannedListManually() {
        manualScanField.setOnAction(event -> {
            String inputLine = manualScanField.getText().trim();
            Optional<Parcel> inputParcelOpt = parcelService.findParcelInInputList(inputLine);
            if (inputParcelOpt.isPresent()) {
                Parcel parcel = inputParcelOpt.get();
                TourNumberDialog.getInstance().showDialog(parcelService, parcel.getTourNumber());
                parcel.setTourNumber(parcel.getTourNumber() + " (MANUAL)");
                parcelService.addParcelToScanned(parcel);
                parcelService.removeParcelFromInput(parcel);
            } else {
                log.warn("Parcel not found {}", inputLine);
            }
            manualScanField.clear();
        });
    }

    private void showDialogOnRescan(String message) {
        Platform.runLater(() -> description.setText(message + " - already scanned!"));
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> description.setText(""));
        pause.play();
    }

    private void setupTable(ObservableList<Parcel> source,
                            TableView<Parcel> table,
                            TableColumn<Parcel,Integer> indexColumn,
                            TableColumn<Parcel, String> trackingNumberColumn,
                            TableColumn<Parcel, String> gibitNumberColumn,
                            TableColumn<Parcel, String> tourNumberColumn) {

        indexColumn.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        trackingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));
        table.setItems(source);
    }

    @FXML private void onUploadFileBtnClicked() {
        ParcelFileLoaderService.uploadFile(parcelService);
    }

    @FXML private void onSaveBtnClicked() {
        TXTWriter.saveParcelsToTXT(parcelService.getScannedList());
    }

    @FXML private void onClearListsBtnClicked() {
        parcelService.clearAllRepositories();
    }

    @FXML private void onLogBtnClicked() {
        LofFileOpener.openLogFile();
    }
}