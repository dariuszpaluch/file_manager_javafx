package com.dariuszpaluch;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Controller {
    @FXML
    Button loadButton;
    @FXML
    Button goUpButton;

    @FXML
    ListView<String> filesListView;

    Path currentPath;
    ObservableList<String> curredFolderList;

    @FXML
    void initialize() {
        loadButton.setOnAction(this::onClickLoadButton);
        goUpButton.setOnAction(this::onClickGoUpButton);

        this.curredFolderList = FXCollections.observableArrayList();
        filesListView.setItems(this.curredFolderList);
        filesListView.setOnMouseClicked(this::onListItemDoubleClick);
    }

    @FXML
    public void onClickLoadButton(ActionEvent event) {
        this.currentPath = Paths.get(System.getProperty("user.dir"));
        this.readAllFilesInFolder(this.currentPath);
    }

    @FXML
    public void onClickGoUpButton(ActionEvent event) {
        this.goUpDir();
    }

    private void onListItemDoubleClick(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
            String selectedDir = filesListView.getSelectionModel().getSelectedItem();
            this.goToNextDir(selectedDir);
        }
    }

    private void goToNextDir(String dir) {
        this.currentPath = this.currentPath.resolve(dir);
        readAllFilesInFolder(this.currentPath);
    }

    private void goUpDir() {
        this.currentPath = this.currentPath.getParent();
        readAllFilesInFolder(this.currentPath);
    }
    private boolean isDir(Path path) {
        if (path == null || !Files.exists(path)) return false;
        else return Files.isDirectory(path);
    }

    public void readAllFilesInFolder(final Path path) {
        this.curredFolderList.clear();


        if(isDir(path)) {
            final File folder = path.toFile();
            for (final File fileEntry : folder.listFiles()) {
                String fileName = fileEntry.getName();
                if (fileEntry.isDirectory()) {
                    this.curredFolderList.add(fileName);
                } else {
                    this.curredFolderList.add(fileName);
                }
            }
        }
    }
}
