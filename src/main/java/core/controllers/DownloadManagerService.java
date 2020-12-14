package core.controllers;

import controllers.ItemDownloadMonitorController;
import core.models.DTO.FileInformation;
import core.models.DTO.SubFileInformation;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;

public class DownloadManagerService extends Service<Long> {

    private FileInformation fileInformation;
    private ItemDownloadMonitorController controller;

    public DownloadManagerService(FileInformation fileInformation, ItemDownloadMonitorController controller) {
        this.setOnSucceeded(event -> {

        });

        this.setOnFailed(event -> {

        });

        this.setOnCancelled(event -> {

        });

        this.fileInformation = fileInformation;
        this.controller = controller;
    }

    @Override
    protected Task<Long> createTask() {

        ArrayList<SubFileInformation> subFileInformations = createSubFileInformations();


        return null;
    }

    private ArrayList<SubFileInformation> createSubFileInformations(){
        long size = fileInformation.getSize();
        int threads = fileInformation.getNumThreads();
        long subSize = (long) Math.ceil(size/(float)threads);

        ArrayList<SubFileInformation> subFileInformations = new ArrayList<>();
        long startPos = 0;
        while (size - startPos > 0){
            subFileInformations.add(new SubFileInformation(startPos, 0, (size - startPos) < 0 ? size : startPos+subSize, fileInformation));
            startPos += subSize;
        }

        return subFileInformations;
    }

}
