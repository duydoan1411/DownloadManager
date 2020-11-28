package controllers;

import core.helpper.Downloader;
import core.models.FileInformation;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.text.DecimalFormat;

public class ItemDownloadMonitorController {

    private FileInformation fileInformation;

    @FXML
    public Label labelURL, labelStatus, labelFileSize, labelDownloaded, labelSpeed, labelRemainTime, labelResume;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Button buttonPause;

    private Downloader downloader;
    private Thread thread;
    private long startPosition;

    @FXML
    private void initialize(){
        labelURL.setText(fileInformation.getUrlPath());
        downloader = new Downloader(fileInformation, startPosition, this);

        progressBar.progressProperty().bind(downloader.progressProperty());
        labelStatus.textProperty().bind(downloader.messageProperty());

        thread = new Thread(downloader);
        thread.setDaemon(true);
        thread.start();
    }

    public void onActionPause(ActionEvent actionEvent){

        if (buttonPause.getText().equals("Tam Dung")){
            downloader.pause();
            buttonPause.setText("Tiep Tuc");
        }else if (buttonPause.getText().equals("Tiep Tuc")){
            downloader.resume();
            buttonPause.setText("Tam Dung");
        }

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

    public void setStartPosition(long startPosition){
        this.startPosition = startPosition;
    }

}
