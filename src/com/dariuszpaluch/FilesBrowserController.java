package com.dariuszpaluch;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesBrowserController {
    public FilesTableView filesTableView;
    public Text currentPathText;
    public Button goUpButton;
    public Button refreshButton;
    public ChoiceBox driveChoiceBox;

    private ObservableList<FileRow> filesRows = FXCollections.observableArrayList();
    private ObservableList<String> driversList = FXCollections.observableArrayList();
    private Path currentPath;
    private Path currentDisk;

    private StringProperty  currentPathStringProperty = new SimpleStringProperty("");

    public FilesBrowserController() {
        driversList.setAll(FileUtils.getListOfDrivers());
    }

    @FXML
    void initialize() {
        this.filesTableView.setItems(this.filesRows);
        this.filesTableView.setOnMouseClicked(this::onTableRowClick);
        this.refreshButton.setOnAction(this::onRefreshButtonClic);
        this.goUpButton.setOnAction(this::goUpDirButtonClick);
        this.driveChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setCurrentDisk(Paths.get(driversList.get((int)newValue)));
            }
        });

        this.driveChoiceBox.setItems(driversList);
        this.setCurrentDisk(Paths.get(this.driversList.get(0)));

        this.currentPathText.textProperty().bind(this.currentPathStringProperty);

        this.updateAll();
    }

    public Path getCurrentDisk() {
        return currentDisk;
    }

    public void setCurrentDisk(Path currentDisk) {
        this.currentDisk = currentDisk;
        this.driveChoiceBox.getSelectionModel().select(currentDisk.toString());

        this.setCurrentPath(currentDisk);
    }

    private void goUpDirButtonClick(ActionEvent actionEvent) {
        this.goUpDir();
    }

    private void onRefreshButtonClic(ActionEvent actionEvent) {
        this.updateAll();
    }

    private void onTableRowClick(MouseEvent mouseEvent) {
        int index = this.filesTableView.getSelectionModel().getSelectedIndex();
        if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 && index >= 0){
            String selectedDir = ((FileRow)this.filesTableView.getSelectionModel().getSelectedItem()).getName();
            this.goIntoSelectedDir(selectedDir);
        }
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
        this.currentPathStringProperty.set(currentPath.toString());
        this.updateAll();
    }

    private void goIntoSelectedDir(String dir) {
        Path tempPath = this.currentPath.resolve(dir);
        if(FileUtils.isDir(tempPath)) {
            this.setCurrentPath(tempPath);
        }
    }

    public void goUpDir() {
        this.setCurrentPath(this.currentPath.getParent());
    }

    private void updateAll() {
        this.updateFilesInCurrentPath();
        this.updateGoUpButton();
    }

    private void updateGoUpButton() {
        goUpButton.setDisable(this.currentPath.getParent() == null);
    }

    private void updateFilesInCurrentPath() {
        this.filesRows.clear();

        if(FileUtils.isDir(this.currentPath)) {
            final File currentFile = this.currentPath.toFile();

            for (final File fileEntry : currentFile.listFiles()) {
                FileRow fileRow = new FileRow(fileEntry);
                if (fileEntry.isDirectory()) {
                    this.filesRows.add(fileRow);
                } else {
                    this.filesRows.add(fileRow);
                }
            }
        }

    }


}
