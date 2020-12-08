package controllers;

import core.helpper.Downloader;
import core.models.FileInformation;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;

public class ItemDownloadMonitorController {

    private FileInformation fileInformation;

    @FXML
    public Label labelURL, labelStatus, labelFileSize, labelDownloaded, labelSpeed, labelRemainTime, labelResume;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Button buttonPause;


    private Stage stage;

    private Downloader downloader;
    private Thread thread;

    private Boolean canResume = false;

    @FXML
    private void initialize(){
        labelURL.setText(fileInformation.getUrlPath());
        if (canResume)
            labelResume.setText("Co");
        else {
            labelResume.setText("Khong");
            fileInformation.setDownloaded(0);
            buttonPause.setDisable(true);
        }
        createDownloader();
    }

    private void createDownloader(){
        MainController.pathFileDownloading.add(fileInformation.getLocalPath()+fileInformation.getFileName());
        downloader = new Downloader(fileInformation, this);

        progressBar.progressProperty().bind(downloader.progressProperty());
        labelStatus.textProperty().bind(downloader.messageProperty());

        thread = new Thread(downloader);
        thread.start();
    }

    public void onActionPause(ActionEvent actionEvent){

        if (buttonPause.getText().equals("Tam Dung")){
            downloader.stop();
            buttonPause.setText("Tiep Tuc");
        }else if (buttonPause.getText().equals("Tiep Tuc")){
            buttonPause.setText("Tam Dung");
            createDownloader();
        }

    }

    public void onActionButtonCancel(ActionEvent actionEvent){
        close();
    }

    public static String getReadableSize(long bytes) {
        if (bytes <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getTimeRemain(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        String timeRemainStr = "";
        if (hours == 0) {
            timeRemainStr = String.format("%d phút %d giây", minutes, seconds);
        } else {
            timeRemainStr = String.format("%d giờ %d phút %d giây", hours, minutes, seconds);
        }
        return timeRemainStr;
    }

    public void setFileInfo(FileInformation fileInfo){
        this.fileInformation = fileInfo;
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return this.stage;
    }

    public void setCanResume(Boolean canResume) {
        this.canResume = canResume;
    }

    public void close() {
        downloader.stop();
        MainController.pathFileDownloading.remove(fileInformation.getLocalPath()+fileInformation.getFileName());
        stage.close();
    }

}
