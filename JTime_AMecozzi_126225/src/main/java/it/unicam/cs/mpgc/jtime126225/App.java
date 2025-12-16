package it.unicam.cs.mpgc.jtime126225;

import it.unicam.cs.mpgc.jtime126225.model.Manager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Manager manager=Manager.getManager();
        Parent root = FXMLLoader.load(getClass().getResource("/screensFXML/home.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
