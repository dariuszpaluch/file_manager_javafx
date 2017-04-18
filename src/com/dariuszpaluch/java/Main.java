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
//        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages");
//        loader.setResources(bundle);
//        primaryStage.setTitle(bundle.getString("applicationName"));
//        LanguageMechanics.setBundle(bundle);
        LanguageMechanics.setState(primaryStage, "applicationName");
        Parent stackPane = loader.load();

        Scene scene = new Scene(stackPane, 1000, 500);
//        String css = class.getResource("/jarcss.css").toExternalForm();
        scene.getStylesheets().clear();
//        scene.getStylesheets().add(css);
        scene.getStylesheets().add(getClass().getResource("/styles/theme.css").toExternalForm());

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
