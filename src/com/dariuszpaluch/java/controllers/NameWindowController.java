package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.utils.LanguageMechanics;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class NameWindowController extends VBox {

  public class CompletedEvent extends Event {

    public CompletedEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
      super(eventType);
    }
  }

  public static EventType<Event> COMPLETED_EVENT_TYPE = new EventType<>("COMPLETED");
  Event completedEvent = new CompletedEvent(COMPLETED_EVENT_TYPE);

  public Button cancelButton;
  public Button saveButton;
  public TextField nameTextField;
  public Text errorText;
  public Text nameLabelTextField;

  private File oldFile;

  public NameWindowController(File oldFile) {
    this.oldFile = oldFile;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/name_window.fxml"));
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
    LanguageMechanics.addItem(saveButton, "save");
    LanguageMechanics.addItem(cancelButton, "cancel");
    LanguageMechanics.addItem(nameLabelTextField, "pleaseEnterFileName");

    this.nameTextField.setText(oldFile.getName());
    this.saveButton.setOnAction(this::onClickSave);
    this.cancelButton.setOnAction(this::onClickCancel);
  }

  private void onClickCancel(ActionEvent actionEvent) {
    try {
      this.finalize();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  private void onClickSave(ActionEvent actionEvent) {
    String newName = nameTextField.getText();
    File newFile = new File(this.oldFile.getParentFile().getPath() + File.separator + newName);

    if (newFile.exists() && !Objects.equals(newName, this.oldFile.getName())) {
      errorText.setText(LanguageMechanics.getValueOfKey("errorFileWithThisNameIsExist"));

    } else {
      errorText.setText("");
      boolean success = this.oldFile.renameTo(newFile);

      if (success) {
        this.fireEvent(completedEvent);
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
      }
      else {
        errorText.setText(LanguageMechanics.getValueOfKey("errorSomeErrorWithSave"));
      }
    }
  }


  public void setName(String name) {
//        this.name = name;
    this.nameTextField.setText(name);
  }

  public File getOldFile() {
    return oldFile;
  }

  public void setOldFile(File oldFile) {
    this.oldFile = oldFile;
    this.nameTextField.setText(oldFile.getName());
  }
}
