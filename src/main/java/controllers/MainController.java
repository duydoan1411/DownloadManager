package controllers;

import UI.Main;
import core.helpper.FileDownload;
import core.helpper.FileInformationHelpper;
import core.models.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainController {

    public static ObservableList<FileInformation> fileInformations = FXCollections.observableArrayList();

    public static ArrayList<String> pathFileDownloading = new ArrayList<>();

    @FXML
    public Button addURLButton;

    @FXML
    public Button startButton;

    @FXML
    public Button deleteButton;

    @FXML
    public TableView tableView;

    @FXML
    public TreeView treeView;

    @FXML
    private void initialize(){
        TableColumn<FileInformation, String> fileNameColumn = new TableColumn<>("Ten File");
        TableColumn<FileInformation, String> sizeColumn = new TableColumn<>("Kich Thuoc");
        TableColumn<FileInformation, String> statusColumn = new TableColumn<>("Trang Thai");
        TableColumn<FileInformation, Date> timeColumn = new TableColumn<>("Thoi Gian");
        TableColumn<FileInformation, String> downloadedColumn = new TableColumn<>("Da Tai Duoc");


        fileInformations.addListener(new ListChangeListener<FileInformation>() {
            @Override
            public void onChanged(Change<? extends FileInformation> c) {
                startButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        fileNameColumn.setPrefWidth(300);
        sizeColumn.setPrefWidth(100);
        statusColumn.setPrefWidth(130);
        timeColumn.setPrefWidth(250);
        downloadedColumn.setPrefWidth(100);

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeCanRead"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusCanRead"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        downloadedColumn.setCellValueFactory(new PropertyValueFactory<>("downloadedCanRead"));

        tableView.getColumns().addAll(fileNameColumn,downloadedColumn, sizeColumn, statusColumn, timeColumn);
        tableView.setItems(fileInformations);

        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Tiep tuc tai");

        mi1.setOnAction(event -> {
            FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();
            FileDownload fileDownload = new FileDownload(fileInformation.getUrlPath());
            if (!fileDownload.getCanResume()){
                fileInformation.setDownloaded(0);
            }
            if ((fileInformation.getStatus() < 100) && !fileIsBusy(fileInformation.getLocalPath()+fileInformation.getFileName()))
                createDownloadForm(fileInformation, fileDownload.getCanResume());
        });
        cm.getItems().add(mi1);

        MenuItem mi2 = new MenuItem("Xoa");

        mi2.setOnAction(event -> {
            FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();
            FileInformationHelpper fileInformationHelpper  = new FileInformationHelpper();
            fileInformationHelpper.delete(fileInformation);
            updateFileList();
        });
        cm.getItems().add(mi2);

        MenuItem mi3 = new MenuItem("Xoa va xoa luon file");
        mi3.setOnAction(event -> {
            FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();
            FileInformationHelpper fileInformationHelpper  = new FileInformationHelpper();
            fileInformationHelpper.delete(fileInformation);
            updateFileList();
            File myObj = new File(fileInformation.getLocalPath()+fileInformation.getFileName());
            myObj.delete();
        });
        cm.getItems().add(mi3);

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();

                if (fileInformation != null) {

                    if (fileInformation.getStatus() == 100 || fileIsBusy(fileInformation.getLocalPath() + fileInformation.getFileName())) {
                        mi1.setDisable(true);
                        startButton.setDisable(true);
                    } else {
                        mi1.setDisable(false);
                        startButton.setDisable(false);
                    }

                    if (fileIsBusy(fileInformation.getLocalPath() + fileInformation.getFileName())) {
                        mi2.setDisable(true);
                    } else {
                        mi2.setDisable(false);
                    }
                    deleteButton.setDisable(mi2.isDisable());
                    mi3.setDisable(mi2.isDisable());

                    if (t.getButton() == MouseButton.SECONDARY) {
                        cm.show(tableView, t.getScreenX(), t.getScreenY());
                    }
                    if (t.getButton() == MouseButton.PRIMARY) {
                        cm.hide();
                    }
                }
            }
        });

        tableView.setRowFactory( tv -> {
            TableRow<FileInformation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    FileInformation fileInformation = row.getItem();
                    if (!fileIsBusy(fileInformation.getLocalPath()+fileInformation.getFileName()) && fileInformation.getStatus() < 100) {
                        FileDownload fileDownload = new FileDownload(fileInformation.getUrlPath());
                        if (!fileDownload.getCanResume()) {
                            fileInformation.setDownloaded(0);
                        }
                        createDownloadForm(fileInformation, fileDownload.getCanResume());
                    }
                }
            });
            return row ;
        });

        updateFileList();

        TreeItem<String> rootItem = new TreeItem<String>("Tat Ca");
        rootItem.setExpanded(true);

        TreeItem<String> successItem = new TreeItem<>("Hoan Tat");
        TreeItem<String> unsuccessItem = new TreeItem<>("Chua Hoan Tat");

        rootItem.getChildren().addAll(successItem, unsuccessItem);

        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String value = ((TreeItem) newValue).getValue().toString();
            if (value.equals(successItem.getValue())){
                updateFileList("success");
            }else if (value.equals(unsuccessItem.getValue())){
                    updateFileList("unsuccess");
                } else if (value.equals(rootItem.getValue())){
                        updateFileList();
                }
        });


    }

    private boolean fileIsBusy(String filePath){
        return pathFileDownloading.contains(filePath);
    }

    private void createDownloadForm(FileInformation fileInformation, Boolean canResume){
        startButton.setDisable(true);
        deleteButton.setDisable(true);
        Stage downloadMonitor = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../item_download_monitor.fxml"));

        ItemDownloadMonitorController itemDownloadMonitorController = new ItemDownloadMonitorController();
        itemDownloadMonitorController.setFileInfo(fileInformation);
        itemDownloadMonitorController.setCanResume(canResume);

        fxmlLoader.setController(itemDownloadMonitorController);

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadMonitor.setTitle(fileInformation.getFileName());
        downloadMonitor.setScene(new Scene(root));
        downloadMonitor.setOnHidden(e -> itemDownloadMonitorController.close());
        itemDownloadMonitorController.setStage(downloadMonitor);
        downloadMonitor.show();

    }

    public static void updateFileList(){
        FileInformationHelpper fileInformationHelpper = new FileInformationHelpper();
        fileInformations.clear();
        fileInformations.addAll(fileInformationHelpper.getAll());
    }
    public static void updateFileList(String options){
        FileInformationHelpper fileInformationHelpper = new FileInformationHelpper();
        fileInformations.clear();
        fileInformations.addAll(fileInformationHelpper.getAll(options));
    }

    public void onActionStartButton(ActionEvent actionEvent){
        FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();
        if (fileInformation!=null){
            FileDownload fileDownload = new FileDownload(fileInformation.getUrlPath());
            if (!fileDownload.getCanResume()) {
                fileInformation.setDownloaded(0);
            }
            createDownloadForm(fileInformation, fileDownload.getCanResume());
        }
    }

    public void onActionDeleteButton(ActionEvent actionEvent){
        FileInformation fileInformation = (FileInformation) tableView.getSelectionModel().getSelectedItem();
        FileInformationHelpper fileInformationHelpper  = new FileInformationHelpper();
        fileInformationHelpper.delete(fileInformation);
        updateFileList();
    }

    public void addURLOnAction(ActionEvent actionEvent) throws IOException {
        Stage addURLStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../add_link_form.fxml"));

        addURLStage.setTitle("Add URL");
        addURLStage.setScene(new Scene(root));
        addURLStage.initOwner(Main.getPrimaryStage());
        addURLStage.initModality(Modality.WINDOW_MODAL);
        addURLStage.setResizable(false);
        addURLStage.show();
    }
}
