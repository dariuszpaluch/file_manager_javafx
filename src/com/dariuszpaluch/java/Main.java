package com.dariuszpaluch.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(new Locale("pl"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/main_layout.fxml"));
        LanguageMechanics.setState(primaryStage, "applicationName");
        Parent stackPane = loader.load();

        Scene scene = new Scene(stackPane, 1000, 500);
        scene.getStylesheets().clear();
//        scene.getStylesheets().add(getClass().getResource("/styles/theme.css").toExternalForm());

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
