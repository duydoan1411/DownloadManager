package controllers;

import core.controllers.DownloadManagerTask;
import core.controllers.DownloadTask;
import core.models.DAO.FileInformationDAO;
import core.models.DTO.FileInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class ItemDownloadMonitorController {

    private FileInformation fileInformation;

    @FXML
    public Label labelURL, labelStatus, labelFileSize, labelDownloaded, labelSpeed, labelRemainTime, labelResume;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Button buttonPause;

    @FXML
    public GridPane tableInfo;

    @FXML
    public ScrollPane scrollPane;

    private Stage stage;

    private DownloadManagerTask downloadTask;
    private Thread thread;

    private Boolean canResume = false;

    @FXML
    private void initialize(){
        labelURL.setText(fileInformation.getUrlPath());
        labelFileSize.setText(getReadableSize(fileInformation.getSize()));
        if (canResume)
            labelResume.setText("Co");
        else {
            labelResume.setText("Khong");
            fileInformation.setDownloaded(0);
            buttonPause.setDisable(true);
        }

        scrollPane.setFitToHeight(true);

        createDownloader();
    }

    private void createDownloader(){
        FileInformationDAO fileInformationDAO = new FileInformationDAO();
        fileInformation = fileInformationDAO.get(fileInformation.getIdFile());
        MainController.pathFileDownloading.add(fileInformation.getLocalPath()+fileInformation.getFileName());
        downloadTask = new DownloadManagerTask(fileInformation, this);

        progressBar.progressProperty().bind(downloadTask.progressProperty());
        labelStatus.textProperty().bind(downloadTask.messageProperty());

        TableColumn tableColumn = new TableColumn();

        thread = new Thread(downloadTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void onActionPause(ActionEvent actionEvent){

        if (buttonPause.getText().equals("Tam Dung")){
            downloadTask.stop();
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
        downloadTask.stop();
        MainController.pathFileDownloading.remove(fileInformation.getLocalPath()+fileInformation.getFileName());
        stage.close();
    }

}
