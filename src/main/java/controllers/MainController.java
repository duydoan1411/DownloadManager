package controllers;

import UI.Main;
import core.helpper.FileInformationHelpper;
import core.models.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class MainController {

    public static ObservableList<FileInformation> fileInformations = FXCollections.observableArrayList();
    public static FileInformationHelpper fileInformationHelpper = new FileInformationHelpper();;

    @FXML
    public Button addURLButton;

    @FXML
    public TableView tableView;

    @FXML
    private void initialize(){
        TableColumn<FileInformation, String> fileNameColumn = new TableColumn<>("Ten File");
        TableColumn<FileInformation, String> sizeColumn = new TableColumn<>("Kich Thuoc");
        TableColumn<FileInformation, String> statusColumn = new TableColumn<>("Trang Thai");
        TableColumn<FileInformation, Date> timeColumn = new TableColumn<>("Thoi Gian");
        TableColumn<FileInformation, String> downloadedColumn = new TableColumn<>("Da Tai Duoc");

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

        tableView.setRowFactory( tv -> {
            TableRow<FileInformation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    FileInformation rowData = row.getItem();

                }
            });
            return row ;
        });

        updateFileList();
    }

    public static void updateFileList(){
        fileInformations.clear();
        fileInformations.addAll(fileInformationHelpper.getAll());
    }

    public static void addToTable(FileInformation fileInformation){
        fileInformations.add(fileInformation);
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
