package core.controllers;

import controllers.ItemDownloadMonitorController;
import controllers.MainController;
import core.models.DAO.FileInformationDAO;
import core.models.DAO.SubFileInformationDAO;
import core.models.DTO.FileInformation;
import core.models.DTO.SubFileInformation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManagerTask extends Task<Void> {

    private static final int BUFFER_SIZE = 4096;
    private FileInformation fileInformation;
    private ItemDownloadMonitorController controller;
    private Boolean stop = false;
    List<Label> labelIds = new ArrayList<>();
    List<Label> labelDownloadeds = new ArrayList<>();
    List<Label> labelStatuses = new ArrayList<>();
    List<ProgressBar> progressBars = new ArrayList<>();
    private List<SubFileInformation> subFileInformations;

    public DownloadManagerTask(FileInformation fileInformation, ItemDownloadMonitorController controller) {
        this.fileInformation = fileInformation;
        this.controller = controller;
        updateMessage("Đang kết nối...");
    }

    @Override
    protected Void call() throws Exception {
        ArrayList<DownloadSubTask> downloadSubTasks = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(fileInformation.getNumThreads());
        DownloadSubTask downloadSubTask;
        if (fileInformation.getSubFileInformationSet().isEmpty()) {
            subFileInformations= createSubFileInformations();
            SubFileInformationDAO subFileInformationDAO = new SubFileInformationDAO();
            for (SubFileInformation subFileInformation : subFileInformations) {
                subFileInformationDAO.add(subFileInformation);
                downloadSubTask = new DownloadSubTask(subFileInformation, controller);
                downloadSubTasks.add(downloadSubTask);
                executor.execute(downloadSubTask);
            }
        } else {
            subFileInformations = fileInformation.getSubFileInformationSet();
            for (SubFileInformation subFileInformation : subFileInformations) {
                downloadSubTask = new DownloadSubTask(subFileInformation, controller);
                downloadSubTasks.add(downloadSubTask);
                executor.execute(downloadSubTask);
            }
        }
        Label label, labelDownloaded, labelStatus;
        ProgressBar progressBar;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.tableInfo.getChildren().clear();
                Label labelIdHeader = new Label("ID");
                Label labelDownloadedHeader = new Label("Đã tải được");
                Label labelStatusHeader = new Label("Trạng thái");
                labelIdHeader.setStyle("-fx-font-weight: bold");
                labelDownloadedHeader.setStyle("-fx-font-weight: bold");
                labelStatusHeader.setStyle("-fx-font-weight: bold");
                controller.tableInfo.add(labelIdHeader, 0, 0);
                controller.tableInfo.add(labelDownloadedHeader, 1, 0);
                controller.tableInfo.add(labelStatusHeader, 2, 0);
            }
        });
        int i = 1;
        for (SubFileInformation subFileInformation : subFileInformations){
            label = new Label(i + "");
            labelIds.add(label);

            labelDownloaded = new Label("0KB");
            labelDownloadeds.add(labelDownloaded);

            labelStatus = new Label("Đang kết nối...");
            labelStatuses.add(labelStatus);

            progressBar = new ProgressBar();
            progressBars.add(progressBar);

            int finalI = i;
            Label finalLabel = label;
            ProgressBar finalProgressBar = progressBar;
            Label finalLabelDownloaded = labelDownloaded;
            Label finalLabelStatus = labelStatus;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.tableInfo.add(finalLabel, 0, finalI);
                    controller.tableInfo.add(finalProgressBar, 2, finalI);
                    controller.tableInfo.add(finalLabelDownloaded, 1, finalI);
                    controller.tableInfo.add(finalLabelStatus, 3, finalI);
                    finalProgressBar.progressProperty().bind(downloadSubTasks.get(finalI-1).progressProperty());
                    finalLabelStatus.textProperty().bind(downloadSubTasks.get(finalI-1).messageProperty());
                    finalLabelDownloaded.textProperty().bind(downloadSubTasks.get(finalI-1).titleProperty());
                }
            });
            i++;
        }


        executor.shutdown();
        long total, total1;
        long startTime, endTime;
        updateMessage("Đang tải xuống...");

        while (!executor.isTerminated()) {
            if (stop){
                for (DownloadSubTask downloadSubTask1 : downloadSubTasks) {
                    downloadSubTask1.stop();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.labelSpeed.setText("0 KB/s");
                        }
                    });
                }
                break;
            }
            //Tinh download speed
            startTime = System.currentTimeMillis();
            total = total1 =0;
            i = 0;
            for (DownloadSubTask downloadSubTask1 : downloadSubTasks) {
                if (downloadSubTask1.isDone()){
                    total += subFileInformations.get(i).getDownloaded();
                }else
                    total += downloadSubTask1.getDownloaded();
                i++;
            }
            updateProgress(total, fileInformation.getSize());
            Thread.sleep(1000);
            i = 0;
            for (DownloadSubTask downloadSubTask1 : downloadSubTasks) {
                if (downloadSubTask1.isDone()){
                    total1 += subFileInformations.get(i).getDownloaded();
                }else
                    total1 += downloadSubTask1.getDownloaded();
                i++;
            }
            fileInformation.setDownloaded(total1);
            endTime = System.currentTimeMillis();
            long speed = (long)((total1 - total)/(float)((endTime - startTime)/1000));
            //Update UI

            long finalTotal = total1;
            int timeRemaining = (int) ((fileInformation.getSize() - total1) / (float)speed);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.labelSpeed.setText(ItemDownloadMonitorController.getReadableSize(speed) + "/s");
                    controller.labelDownloaded.setText(ItemDownloadMonitorController.getReadableSize(finalTotal));
                    controller.labelRemainTime.setText(ItemDownloadMonitorController.getTimeRemain(timeRemaining));
                }
            });
        }
        if (fileInformation.getDownloaded() == fileInformation.getSize()){
            updateMessage("Đang nối file...");
            String home = System.getProperty("user.home");
            String localPathSubFile;
            FileInputStream fileInputStream;
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            long totalWrite = 0;
            try {
                PrintWriter pw;
                FileOutputStream fileOutputStream = new FileOutputStream(home + "/Downloads/"+ fileInformation.getFileName());
                for (SubFileInformation subFileInformation: subFileInformations) {
                    localPathSubFile = home+"/Downloads/"+"Temporary-"+fileInformation.getIdFile()+"-"
                            +fileInformation.getFileName()+"/"+fileInformation.getId() + subFileInformation.getIdSubFile();

                    fileInputStream = new FileInputStream(localPathSubFile);

                    while ((bytesRead = fileInputStream.read(buffer)) != -1){
                        fileOutputStream.write(buffer, 0, bytesRead);
                        totalWrite += bytesRead;

                        updateProgress(totalWrite, fileInformation.getSize());
                    }
                }
                File file = new File(home + "/Downloads/"+ fileInformation.getFileName());
                if (file.length() == fileInformation.getSize()){
                    File fileSubs = new File(home+"/Downloads/"+"Temporary-"+fileInformation.getIdFile()+"-"+fileInformation.getFileName());;
                    deleteDirectory(fileSubs);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    public boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }

    @Override
    protected void succeeded() {
        FileInformationDAO fileInformationDAO = new FileInformationDAO();
        if (fileInformation.getDownloaded() == fileInformation.getSize()) {
            fileInformation.setStatus(100);
            controller.close();
        }
        fileInformationDAO.update(fileInformation);
        MainController.updateFileList();
    }

    private ArrayList<SubFileInformation> createSubFileInformations(){
        long size = fileInformation.getSize();
        int threads = fileInformation.getNumThreads();
        long subSize = (long) Math.ceil(size/(float)threads);

        ArrayList<SubFileInformation> subFileInformations = new ArrayList<>();
        long startPos = 0;
        while (size - startPos > 0){
            subFileInformations.add(new SubFileInformation(startPos, 0, (size - startPos) < 0 ? size : startPos+subSize-1, fileInformation));
            startPos += subSize;
        }

        return subFileInformations;
    }

    public Boolean isStop() {
        return stop;
    }

    public void stop() {
        stop = true;
    }
}
