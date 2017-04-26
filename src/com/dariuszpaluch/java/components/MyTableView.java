package com.dariuszpaluch.java.components;


import com.dariuszpaluch.java.models.FileRow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;


public class MyTableView extends TableView {
  private static List<MyTableView> components = new ArrayList<>();

  private ObservableList<FileRow> rows;

  private static void unselectRestComponents(MyTableView newSelectedTableView) {
    for (MyTableView item : components) {
      if (newSelectedTableView != item) {
        Platform.runLater(() -> {
          item.getSelectionModel().clearSelection();
        });
      }
    }
  }

  public MyTableView() {
    this.initialize();

  }

  private void onSelectRow() {
    unselectRestComponents(this);
  }

  private void initialize() {
    components.add(this);
    this.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null && (int) newSelection > -1) {
        this.onSelectRow();
      }
    });
  }

}
