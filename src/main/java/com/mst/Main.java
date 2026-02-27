package com.mst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Minimum Spanning Tree");

        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("views/HomeView.fxml"));
        Scene homeScene = new Scene(fxml.load());

        primaryStage.setScene(homeScene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
