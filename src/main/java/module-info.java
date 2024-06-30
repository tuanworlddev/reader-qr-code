module com.tuandev.readerqrcode {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.apache.pdfbox;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;

    exports com.tuandev.readerqrcode.views;
    exports com.tuandev.readerqrcode.controllers;

    opens com.tuandev.readerqrcode.views to
            javafx.fxml;
    opens com.tuandev.readerqrcode.models to
            javafx.base;
}