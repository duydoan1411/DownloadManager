package controllers;

import core.controllers.FileDownloadHelper;
import core.models.DAO.FileInformationDAO;
import core.models.DTO.FileInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileInformationController {

    private String url;

    @FXML
    public TextField textFieldURL;

    @FXML
    public TextField textFieldSaveAs;

    @FXML
    public Label labelFileSize;

    @FXML
    public ComboBox comboBoxThread;

    private FileInformation fileInformation;
    private FileDownloadHelper fileDownloadHelper;

    @FXML
    private void initialize() throws IOException {
        fileDownloadHelper = new FileDownloadHelper(url);
        fileInformation = fileDownloadHelper.getFileInformation();

        textFieldURL.setText(url);
        textFieldSaveAs.setText(fileInformation.getLocalPath()+fileInformation.getFileName());
        textFieldSaveAs.setEditable(false);
        labelFileSize.setText(ItemDownloadMonitorController.getReadableSize(fileInformation.getSize()));
        comboBoxThread.getItems().addAll(1, 2, 4, 8, 16, 32);
        comboBoxThread.setValue(1);
        comboBoxThread.setDisable(!fileDownloadHelper.getCanResume());
    }

    public void onActionCancelButton(ActionEvent actionEvent){
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void onActionFileChoose(ActionEvent actionEvent){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save File");
        File file = directoryChooser.showDialog((Stage)(((Node)actionEvent.getSource()).getScene().getWindow()));
        if (file != null){
            textFieldSaveAs.setText(file.getAbsolutePath()+"/"+fileInformation.getFileName());
            fileInformation.setLocalPath(file.getAbsolutePath()+"/");
        }
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void onActionDownloadButton(ActionEvent actionEvent) throws IOException {

        fileInformation.setNumThreads((int)comboBoxThread.getValue());
        File file = new File(fileInformation.getLocalPath()+fileInformation.getFileName());
        if (file.exists()){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Xac Nhan");
            alert.setHeaderText("File da ton tai, ban co muon ghi de?");
            alert.setContentText("File da ton tai hoac dang duoc tai, neu ban ghi de co the dan toi hong du lieu hoac loi!!!");
            ButtonType yesButton = new ButtonType("Co");
            ButtonType noButton = new ButtonType("Khong");

            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(yesButton, noButton);
            Optional<ButtonType> optional = alert.showAndWait();

            if (optional.get() == null) return;
            if (optional.get() == noButton) return;
        }

        FileInformationDAO fileInformationDAO = new FileInformationDAO();
        fileInformationDAO.add(fileInformation);
        MainController.updateFileList();

        Stage downloadMonitor = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../item_download_monitor.fxml"));

        ItemDownloadMonitorController itemDownloadMonitorController = new ItemDownloadMonitorController();
        itemDownloadMonitorController.setFileInfo(fileInformation);
        itemDownloadMonitorController.setCanResume(fileDownloadHelper.getCanResume());

        fxmlLoader.setController(itemDownloadMonitorController);

        Parent root = fxmlLoader.load();

        Node button = (Node)actionEvent.getSource();
        ((Stage)button.getScene().getWindow()).close();
        downloadMonitor.setTitle(fileInformation.getFileName());
        downloadMonitor.setScene(new Scene(root));
        downloadMonitor.setOnHidden(e -> itemDownloadMonitorController.close());
        itemDownloadMonitorController.setStage(downloadMonitor);
        downloadMonitor.show();
    }

}
