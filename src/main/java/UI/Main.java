package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../main_form.fxml"));

        Main.primaryStage = primaryStage;

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../img/app-icon.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("File Download Manager");
        primaryStage.show();
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }
}
