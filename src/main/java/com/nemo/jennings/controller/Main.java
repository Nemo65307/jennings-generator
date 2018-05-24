package com.nemo.jennings.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/firstPolynomial.fxml"));
        primaryStage.setTitle("Jennings Generator");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 650, 500));
        primaryStage.getScene().getStylesheets().add("/view/custom.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
