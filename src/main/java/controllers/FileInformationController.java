package controllers;

import core.helpper.Downloader;
import core.helpper.FileDownload;
import core.helpper.FileInformationHelpper;
import core.models.FileInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

    @FXML
    private void initialize() throws IOException {
        FileDownload fileDownload = new FileDownload(url);
        fileInformation = fileDownload.getFileInformation();

        textFieldURL.setText(url);
        textFieldSaveAs.setText(fileInformation.getLocalPath()+fileInformation.getFileName());
        textFieldSaveAs.setEditable(false);
        labelFileSize.setText(ItemDownloadMonitorController.getReadableSize(fileInformation.getSize()));
        comboBoxThread.getItems().addAll(1, 2, 4, 8, 16, 32);
        comboBoxThread.setValue(1);
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

        FileInformationHelpper fileInformationHelpper = new FileInformationHelpper();
        fileInformationHelpper.add(fileInformation);
        MainController.addToTable(fileInformation);


        Stage downloadMonitor = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../item_download_monitor.fxml"));

        ItemDownloadMonitorController itemDownloadMonitorController = new ItemDownloadMonitorController();
        itemDownloadMonitorController.setFileInfo(fileInformation);
        itemDownloadMonitorController.setStartPosition(0);

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
