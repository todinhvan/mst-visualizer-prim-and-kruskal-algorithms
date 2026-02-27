package com.mst.controllers;

import com.mst.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpController {

    public void backClickBtn(ActionEvent e) throws IOException {
        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("views/HomeView.fxml"));
        Scene homeScene = new Scene(fxml.load());

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(homeScene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
