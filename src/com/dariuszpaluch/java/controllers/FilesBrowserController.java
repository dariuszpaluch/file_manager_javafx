package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.components.MyTableView;
import com.dariuszpaluch.java.utils.LanguageMechanics;
import com.dariuszpaluch.java.models.FileRow;
import com.dariuszpaluch.java.utils.FileUtils;
import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesBrowserController {
  public MyTableView filesTableView;
  public Text currentPathText;
  public Button goUpButton;
  public Button refreshButton;
  public ChoiceBox<String> driveChoiceBox;
  public Tooltip goUpButtonTooltip;
  public Tooltip refreshButtonTooltip;
  public TableColumn<FileRow, String> nameTableColumn;
  public TableColumn<FileRow, String> extTableColumn;
  public TableColumn<FileRow, String> sizeTableColumn;
  public TableColumn<FileRow, String> dateTableColumn;
  public TableColumn<FileRow, String> attrTableColumn;

  private ObservableList<FileRow> filesRows = FXCollections.observableArrayList();
  private ObservableList<String> driversList = FXCollections.observableArrayList();
  private Path currentPath;
  private Path currentDisk;

  private StringProperty currentPathStringProperty = new SimpleStringProperty("");

  public class SelectRowEvent extends Event {

    public SelectRowEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
      super(eventType);
    }
  }

  public static EventType<Event> SELECT_ROW_EVENT_TYPE = new EventType<>("SELECT_ROW_EVENT_TYPE");
  Event selectRowEvent = new FilesBrowserController.SelectRowEvent(SELECT_ROW_EVENT_TYPE);

  public FilesBrowserController() {
    driversList.setAll(FileUtils.getListOfDrivers());
  }

  @FXML
  void initialize() {
    filesTableView.setDisable(true);
    nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//    extTableColumn.setCellValueFactory(new PropertyValueFactory<>("ext"));
    sizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
    dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    attrTableColumn.setCellValueFactory(new PropertyValueFactory<>("attr"));

    LanguageMechanics.addItem(nameTableColumn, "name");
    LanguageMechanics.addItem(sizeTableColumn, "size");
    LanguageMechanics.addItem(dateTableColumn, "date");
    LanguageMechanics.addItem(attrTableColumn, "attr");

    this.filesTableView.setItems(this.filesRows);
    this.filesTableView.setOnMouseClicked(this::onTableRowClick);
    this.refreshButton.setOnAction(this::onRefreshButtonClic);
    this.goUpButton.setOnAction(this::goUpDirButtonClick);
    this.driveChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        setCurrentDisk(Paths.get(driversList.get((int) newValue)));
      }
    });

    this.driveChoiceBox.setItems(driversList);
    this.setCurrentDisk(Paths.get(this.driversList.get(0)));

    this.currentPathText.textProperty().bind(this.currentPathStringProperty);

    LanguageMechanics.addItem(goUpButtonTooltip, "goUpDirTooltip");
    LanguageMechanics.addItem(refreshButtonTooltip, "refreshTooltip");
    LanguageMechanics.updateAllItems();
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
    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 && index >= 0) {
      String selectedDir = ((FileRow) this.filesTableView.getSelectionModel().getSelectedItem()).getName();
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
    if (FileUtils.isDir(tempPath)) {
      this.setCurrentPath(tempPath);
    }
  }

  public void goUpDir() {
    this.setCurrentPath(this.currentPath.getParent());
  }

  public void updateAll() {
    this.filesRows.clear();
    this.filesTableView.setDisable(false);

    Task task = new Task() {
      @Override
      protected Object call() throws Exception {
        return updateFilesInCurrentPath(currentPath);
      }
    };

    new Thread(task).start();

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        filesTableView.setItems((ObservableList<FileRow>) task.getValue());
      }
    });
  }

  private void updateGoUpButton() {
    goUpButton.setDisable(this.currentPath.getParent() == null);
  }

  private ObservableList<FileRow> updateFilesInCurrentPath(Path currentPath) {
    ObservableList<FileRow> filesRows = FXCollections.observableArrayList();

    if (FileUtils.isDir(currentPath)) {
      final File currentFile = currentPath.toFile();

      for (final File fileEntry : currentFile.listFiles()) {
        FileRow fileRow = new FileRow(fileEntry);
        if (fileEntry.isDirectory()) {
          filesRows.add(fileRow);
        } else {
          filesRows.add(fileRow);
        }
      }
    }

    return filesRows;

  }

  public Path getSelectedPaths() {
    Object selectedItem = this.filesTableView.getSelectionModel().getSelectedItem();

    if (selectedItem == null) {
      return null;
    }

    return ((FileRow) selectedItem).getPath();
  }


}
