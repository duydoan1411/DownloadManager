package controllers;

import UI.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddLinkController {

    public TextField textFieldURL;
    public Button addButton;

    public void onActionAddButton(ActionEvent actionEvent) throws IOException {

        Pattern pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(textFieldURL.getText());
        if (!matcher.find()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Kiểm tra lại URL");
            alert.showAndWait();
            return;
        }

        Stage addURLStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../file_information_form.fxml"));

        FileInformationController fxmlController = new FileInformationController();
        fxmlController.setUrl(textFieldURL.getText());
        fxmlLoader.setController(fxmlController);

        Parent root = fxmlLoader.load();

        Node button = (Node)actionEvent.getSource();
        ((Stage)button.getScene().getWindow()).close();
        addURLStage.setTitle("File Information");
        addURLStage.setScene(new Scene(root));
        addURLStage.setResizable(false);
        addURLStage.show();
    }

    public void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            addButton.fire();
        }
    }
}
