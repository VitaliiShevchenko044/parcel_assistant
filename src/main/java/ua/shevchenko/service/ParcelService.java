package ua.shevchenko.service;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shevchenko.io.writer.TourPrinter;
import ua.shevchenko.model.Parcel;
import ua.shevchenko.repository.ParcelRepository;
import ua.shevchenko.dialog.TourNumberDialog;

import java.util.Optional;
import java.util.function.Consumer;

public class ParcelService {

    private static final Logger log = LoggerFactory.getLogger(ParcelService.class);

    private final ParcelRepository inputRepository;
    private final ParcelRepository scannedRepository;
    private Consumer<String> rescanListener = trackingNumber -> {};

    public ParcelService(ParcelRepository inputRepository, ParcelRepository scannedRepository) {
        this.inputRepository = inputRepository;
        this.scannedRepository = scannedRepository;
    }

    public ObservableList<Parcel> getInputList() {
        return inputRepository.getAll();
    }

    public ObservableList<Parcel> getScannedList() {
        return scannedRepository.getAll();
    }

    public void addParcelsToInput(ObservableList<Parcel> parcels) {
        parcels.forEach(inputRepository::add);
    }

    public void addParcelToInput(Parcel parcel) {
        inputRepository.add(parcel);
    }

    public void addParcelToScanned(Parcel parcel) {
        scannedRepository.add(parcel);
    }

    public void removeParcelFromInput(Parcel parcel) {inputRepository.remove(parcel);}

    public Optional<Parcel> findParcelInInputList(String trackingNumber) {
        return inputRepository.findParcel(trackingNumber);
    }

    public void clearAllRepositories() {
        inputRepository.clear();
        scannedRepository.clear();
    }

    public void setRescanListener(Consumer<String> rescanListener) {this.rescanListener = rescanListener;}

    /**
     * Обробляє відканований трек-номер.
     * Якщо посилка знайдена в input - перемістити в scanned.
     * Якщо посилка вже відсканована - викликати rescanListener.
     * Інакше - попередити в лог.
     */
    public void processScannedParcel(String scannedCode) {
        Optional<Parcel> inputParcelOpt = inputRepository.findParcel(scannedCode);
        if (inputParcelOpt.isPresent()) {
            Parcel parcel = inputParcelOpt.get();
            scannedRepository.add(parcel);
            inputRepository.remove(parcel);
            log.info("The parcel scanned - {}", parcel.getTrackingNumber());
            TourNumberDialog.getInstance().showDialog(this, parcel.getTourNumber());
            TourPrinter.printTourNumber(parcel.getTourNumber());
        } else {
            Optional<Parcel> scannedParcelOpt = scannedRepository.findParcel(scannedCode);
            if (scannedParcelOpt.isPresent()) {
                rescanListener.accept(scannedParcelOpt.get().getTrackingNumber());
                log.info("The parcel has already been scanned - {}", scannedParcelOpt.get().getTrackingNumber());
            } else {
                log.warn("Parcel not found {}", scannedCode);
            }
        }
    }
}
