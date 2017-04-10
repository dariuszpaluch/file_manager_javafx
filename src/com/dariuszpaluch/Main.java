package com.dariuszpaluch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("File Manager - by Dariusz");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("main_layout.fxml"));
        Parent stackPane = loader.load();

        Scene scene = new Scene(stackPane, 1000, 500);

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
