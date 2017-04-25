package com.dariuszpaluch.java;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FilesTableView extends TableView {
    private static List<FilesTableView> components = new ArrayList<>();

    private ObservableList<FileRow> rows;

    private static void unselectRestComponents(FilesTableView newSelectedTableView) {
        for(FilesTableView item: components) {
            if(newSelectedTableView != item) {
                Platform.runLater(() -> {
                    item.getSelectionModel().clearSelection();
                });
            }
        }
    }

    public FilesTableView() {
        this.initialize();

    }

    private void onSelectRow() {
        unselectRestComponents(this);
    }

    private void initialize() {
        components.add(this);
        this.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && (int)newSelection > -1) {
                this.onSelectRow();
            }
        });
    }

}
