package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.utils.LanguageMechanics;
import com.dariuszpaluch.java.tasks.WalkFileTreeTheadTask;
import com.dariuszpaluch.java.utils.BindingNumberUtils;
import com.dariuszpaluch.java.visitors.GetSizeDirVisitor;
import com.dariuszpaluch.java.visitors.MySimpleFileVisitor;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Path;

public class OperationProgressController extends VBox {
  private MainLayoutController.OperationTypeEnum type = null;

  private Path path;
  public ProgressBar progressBar;
  public Button cancelButton;
  public Text sizeTitleText;
  public Text totalSizeText;
  public Text operationTypeFilesText;
  public Text removingPathText;
  public Tooltip cancelButtonTooltip;

  private ReadOnlyLongProperty operationFilesSize;
  private ReadOnlyLongProperty processedFilesSizeProperty;

  private MySimpleFileVisitor operationSimpleFileVisitor;

  private WalkFileTreeTheadTask getSizeTreeTheadTask;
  private WalkFileTreeTheadTask operationTreeTheadTask;

  public class CompletedEvent extends Event {

    public CompletedEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
      super(eventType);
    }
  }

  public static EventType<Event> COMPLETED_EVENT_TYPE = new EventType<>("COMPLETED");
  Event completedEvent = new CompletedEvent(COMPLETED_EVENT_TYPE);

  public OperationProgressController(Path path, MySimpleFileVisitor operationSimpleFileVisitor, MainLayoutController.OperationTypeEnum type) {
    this.type = type;
    this.path = path;

    MySimpleFileVisitor getSizeDirVisitor = new GetSizeDirVisitor();
    this.getSizeTreeTheadTask = new WalkFileTreeTheadTask(this.path, getSizeDirVisitor);
    this.operationFilesSize = getSizeDirVisitor.getProcessedFilesSizeProperty();
    this.operationSimpleFileVisitor = operationSimpleFileVisitor;
    this.operationTreeTheadTask = new WalkFileTreeTheadTask(this.path, operationSimpleFileVisitor);

    this.processedFilesSizeProperty = this.operationSimpleFileVisitor.getProcessedFilesSizeProperty();

    getSizeTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        onGetTotalSizeSuccess();
      }
    });

    operationTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        onOperationSuccess();
      }
    });

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/operation_progress.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @FXML
  void initialize() {
    String operationBundleKey = "";

    if(this.type == MainLayoutController.OperationTypeEnum.COPY) {
      //copy
      operationBundleKey = "copieng";
    }
    else if(this.type == MainLayoutController.OperationTypeEnum.CUT) {
      //cut
      operationBundleKey = "cutting";
    }else {
      //delete
      operationBundleKey = "deleting";
    }

    LanguageMechanics.addItem(operationTypeFilesText, operationBundleKey);
    LanguageMechanics.addItem(cancelButtonTooltip, "cancel");
    LanguageMechanics.addItem(sizeTitleText, "size");

    removingPathText.setText(this.path.toString());
    progressBar.setProgress(-1.0);


    cancelButton.setOnAction(this::onCancelButtonClick);
    getSizeTreeTheadTask.start();
  }

  private void onCancelButtonClick(ActionEvent actionEvent) {
    this.cancelButton.setDisable(true);
    this.getSizeTreeTheadTask.cancel();
    this.operationTreeTheadTask.cancel();

    double temp = this.progressBar.getProgress();
//    this.removingFilesText.setText("ZAKONCZONE");
  }

  private void onGetTotalSizeSuccess() {
    this.operationTreeTheadTask.start();
//        new Thread(this.operationTreeTheadTask).start();
    Long result = (Long) getSizeTreeTheadTask.getValue();
    Platform.runLater(() -> {
      totalSizeText.setText(Long.toString(result));
    });

    progressBar.progressProperty().bind(BindingNumberUtils.calculateProgress(this.operationFilesSize, this.processedFilesSizeProperty));
  }

  private void onOperationSuccess() {
    cancelButton.setVisible(false);

    Platform.runLater(() -> {
      String operationBundleKey = "";

      if(this.type == MainLayoutController.OperationTypeEnum.COPY) {
        //copy
        operationBundleKey = "copiengCompleted";
      }
      else if(this.type == MainLayoutController.OperationTypeEnum.CUT) {
        //cut
        operationBundleKey = "cuttingCompleted";
      }else {
        //delete
        operationBundleKey = "deletingCompleted";
      }

      LanguageMechanics.addItem(operationTypeFilesText, operationBundleKey);
    });
    this.fireEvent(completedEvent);
  }

}
