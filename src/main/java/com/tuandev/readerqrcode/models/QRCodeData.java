package com.tuandev.readerqrcode.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QRCodeData {
    private StringProperty stt;
    private StringProperty value;

    public QRCodeData(String value) {
        this.value = new SimpleStringProperty(value);
    }

    public String getStt() {
        return (String)this.stt.get();
    }

    public StringProperty sttProperty() {
        return this.stt;
    }

    public void setStt(String stt) {
        this.stt.set(stt);
    }

    public String getValue() {
        return (String)this.value.get();
    }

    public StringProperty valueProperty() {
        return this.value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
