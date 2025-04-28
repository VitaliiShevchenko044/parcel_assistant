package ua.shevchenko.io.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class TourPrinter {

    private static final Logger log = LoggerFactory.getLogger(TourPrinter.class);

    public static void printTourNumber(String tourNumber) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0)
                    return Printable.NO_SUCH_PAGE;

                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setFont(new Font("Times New Roman", Font.PLAIN, 96));

                FontMetrics metrics = graphics2D.getFontMetrics();
                int textWight = metrics.stringWidth(tourNumber);
                int textHeight = metrics.getAscent();

                double pageCenterX = pageFormat.getImageableX() + pageFormat.getImageableWidth() / 2;
                double pageCenterY = pageFormat.getImageableX() + pageFormat.getImageableHeight() / 2;

                int startX = (int) (pageCenterX - (double) textWight / 2);
                int startY = (int) (pageCenterY + (double) textHeight / 2);

                graphics2D.drawString(tourNumber, startX, startY);

                return Printable.PAGE_EXISTS;
            });

            try {
                printerJob.print();
            } catch (PrinterException e) {
                log.error(e.getMessage());
            }
    }
}