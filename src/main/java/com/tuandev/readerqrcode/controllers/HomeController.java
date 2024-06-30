package com.tuandev.readerqrcode.controllers;

import com.google.zxing.NotFoundException;
import com.tuandev.readerqrcode.models.QRCodeData;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public ScrollPane scrollImageContainer;
    public VBox imageContainer;
    public TableColumn<QRCodeData, String> sttColumn;
    public TableColumn<QRCodeData, String> valueColumn;
    public TableView<QRCodeData> dataTable;
    private FileChooser fileChooser;
    private File pdfFile;
    private List<File> imageQRCodeList;
    private ObservableList<QRCodeData> dataList;

    public HomeController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String accessKey = AuthController.getInstance().readKey();
            if (accessKey != null && AuthController.getInstance().verifyAccessKey(accessKey)) {
                AuthController.getInstance().isLoggedIn = true;
            }
        } catch (IOException var4) {
            IOException e = var4;
            this.showErrorDialog(e.getMessage());
        }

        this.imageContainer.prefWidthProperty().bind(this.scrollImageContainer.widthProperty());
        this.fileChooser = new FileChooser();
        this.dataList = FXCollections.observableArrayList();
        this.sttColumn.setCellValueFactory((cellData) -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1;
            return Bindings.createStringBinding(() -> {
                return Integer.toString(index);
            }, new Observable[0]);
        });
        this.valueColumn.setCellValueFactory(new PropertyValueFactory("value"));
        this.dataTable.setItems(this.dataList);
    }

    public void onOpenPDF(ActionEvent actionEvent) {
        if (!AuthController.getInstance().isLoggedIn) {
            this.showLoginPop();
        } else {
            this.fileChooser.setTitle("Open PDF");
            this.fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("PDF Files", new String[]{"*.pdf"})});
            this.pdfFile = this.fileChooser.showOpenDialog((Window)null);
            if (this.pdfFile != null) {
                try {
                    this.imageQRCodeList = QRCode.convertImageFromPDF(this.pdfFile);
                    this.dataList.clear();
                    this.imageContainer.getChildren().clear();
                    this.imageQRCodeList.forEach((imageFile) -> {
                        this.imageContainer.getChildren().add(this.imageItem(imageFile));

                        try {
                            String data = QRCode.readQRCodeFromImage(imageFile);
                            this.dataList.add(new QRCodeData(data));
                        } catch (NotFoundException | IOException var3) {
                            this.showErrorDialog(var3.getMessage());
                        }

                    });
                } catch (IOException var3) {
                    this.showErrorDialog(var3.getMessage());
                }
            }
        }

    }

    public void onExportExcel(ActionEvent actionEvent) {
        if (!AuthController.getInstance().isLoggedIn) {
            this.showLoginPop();
        } else {
            this.fileChooser.setTitle("Export Excel");
            this.fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Text Files", new String[]{"*.txt"})});
            File excelFile = this.fileChooser.showSaveDialog((Window)null);
            if (excelFile != null && !this.dataList.isEmpty()) {
                try {
                    Excel.writeTextFile(excelFile, this.dataList);
                    this.showOpenExcelDialog(excelFile);
                } catch (IOException var4) {
                    this.showErrorDialog(var4.getMessage());
                }
            }
        }
    }



    private ImageView imageItem(File file) {
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200.0);
        imageView.setFitHeight(250.0);
        return imageView;
    }

    private void showErrorDialog(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText((String)null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private void showOpenExcelDialog(File excelFile) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Open Excel File");
        alert.setHeaderText((String)null);
        alert.setContentText("Excel file has been saved. Do you want to open it now?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(new ButtonType[]{buttonTypeYes, buttonTypeNo});
        alert.showAndWait().ifPresent((buttonType) -> {
            if (buttonType == buttonTypeYes) {
                try {
                    Desktop.getDesktop().open(excelFile);
                } catch (IOException var5) {
                    this.showErrorDialog(var5.getMessage());
                }
            }

        });
    }

    private void showLoginPop() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        vBox.setSpacing(10.0);
        vBox.setPrefWidth(300.0);
        Label keyLabel = new Label("Access Key:");
        vBox.getChildren().add(keyLabel);
        PasswordField keyField = new PasswordField();
        keyField.setPromptText("••••••••••");
        vBox.getChildren().add(keyField);
        CheckBox rememberCheckBox = new CheckBox("Remember me");
        vBox.getChildren().add(rememberCheckBox);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setContent(vBox);
        alert.setTitle("Login");
        Objects.requireNonNull(keyField);
        Platform.runLater(keyField::requestFocus);
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent((buttonType) -> {
            if (buttonType == ButtonType.OK) {
                String key = keyField.getText();
                boolean remember = rememberCheckBox.isSelected();
                if (key.equals("HongRancho")) {
                    AuthController.getInstance().isLoggedIn = true;
                    if (remember) {
                        try {
                            AuthController.getInstance().writeKey(key);
                        } catch (IOException var7) {
                            IOException e = var7;
                            this.showErrorDialog(e.getMessage());
                        }
                    }

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText((String)null);
                    successAlert.setContentText("You have successfully logged in!");
                    successAlert.showAndWait();
                } else {
                    this.showErrorDialog("Access Key Invalid");
                }
            }

        });
    }

    public void onLogout(ActionEvent actionEvent) {
        try {
            AuthController.getInstance().isLoggedIn = false;
            AuthController.getInstance().writeKey("");
            this.showLoginPop();
        } catch (IOException var3) {
            IOException e = var3;
            this.showErrorDialog(e.getMessage());
        }

    }

    public void onLogin(ActionEvent actionEvent) {
        this.showLoginPop();
    }
}
