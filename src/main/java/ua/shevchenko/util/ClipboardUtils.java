package ua.shevchenko.util;

import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;

public class ClipboardUtils {

    public static void setupCopyHandler(TableView<?> inputParcelsTable, TableView<?> scannedParcelsTable) {
        attachCopyHandlers(inputParcelsTable);
        attachCopyHandlers(scannedParcelsTable);
    }

    public static void attachCopyHandlers(TableView<?> table) {
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                copySelectedCellToClipboard(table);
            }
        });

        table.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.C) {
                copySelectionToClipboard(table);
            }
        });
    }

    private static void copySelectedCellToClipboard(TableView<?> table) {
        if (table.getSelectionModel().getSelectedCells().isEmpty()) return;
        TablePosition<?, ?> position = table.getSelectionModel().getSelectedCells().getFirst();
        Object cell = table.getVisibleLeafColumn(position.getColumn()).getCellData(position.getRow());

        if (cell != null) {
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.toString());
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    private static void copySelectionToClipboard(TableView<?> table) {
        StringBuilder clipboardString = new StringBuilder();
        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();
        int prevRow = -1;

        for (TablePosition position : positionList) {
            int row = position.getRow();
            Object cell = table.getVisibleLeafColumn(position.getColumn()).getCellData(row);

            if (cell == null)
                cell = "";

            if (prevRow == row)
                clipboardString.append('\t');
            else if (prevRow != -1)
                clipboardString.append('\n');

            clipboardString.append(cell);
            prevRow = row;
        }

        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
}
