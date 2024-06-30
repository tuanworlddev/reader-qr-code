package com.tuandev.readerqrcode.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Objects;

public class HomeView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Read QR Code From PDF");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("images/ic-app.png"))));
        stage.show();
    }
}
