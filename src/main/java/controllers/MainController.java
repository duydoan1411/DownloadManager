package controllers;

import UI.Main;
import core.models.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class MainController {

    public static ObservableList<FileInformation> fileInformations;

    @FXML
    public Button addURLButton;

    @FXML
    public TableView tableView;

    @FXML
    private void initialize(){
        TableColumn<FileInformation, String> fileNameColumn = new TableColumn<>("Ten File");
        TableColumn<FileInformation, Long> sizeColumn = new TableColumn<>("Kich Thuoc");
        TableColumn<FileInformation, String> statusColumn = new TableColumn<>("Trang Thai");
        TableColumn<FileInformation, Date> timeColumn = new TableColumn<>("Thoi Gian");

        fileNameColumn.setPrefWidth(300);
        sizeColumn.setPrefWidth(100);
        statusColumn.setPrefWidth(100);
        timeColumn.setPrefWidth(250);

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        tableView.getColumns().addAll(fileNameColumn, sizeColumn, statusColumn, timeColumn);
        getFileList();

        tableView.setItems(fileInformations);
    }

    private void getFileList(){
        fileInformations = FXCollections.observableArrayList();
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
