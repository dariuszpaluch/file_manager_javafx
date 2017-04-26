package com.dariuszpaluch.java.controllers;

import com.dariuszpaluch.java.utils.LanguageMechanics;
import com.dariuszpaluch.java.utils.DialogUtils;
import com.dariuszpaluch.java.visitors.CopyDirVisitor;
import com.dariuszpaluch.java.visitors.DeleteDirVisitor;
import com.dariuszpaluch.java.visitors.MoveDirVisitor;
import com.dariuszpaluch.java.visitors.MySimpleFileVisitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

public class MainLayoutController {
  //    public Tooltip pasteButtonTooltip;
  public Tooltip cutButtonTooltip;
  public Tooltip deleteButtonTooltip;
  public Tooltip copyButtonTooltip;

  enum OperationTypeEnum {
    COPIE,
    MOVE
  }

  ;

  class OperationStorage {
    private Path path;
    private OperationTypeEnum type;

    public OperationStorage(Path path, OperationTypeEnum type) {
      this.path = path;
      this.type = type;
    }

    public Path getPath() {
      return path;
    }

    public OperationTypeEnum getType() {
      return type;
    }
  }

  public Button deleteButton;
  public Button changeNameButton;

  public FilesBrowserController leftFilesBrowserController;
  public FilesBrowserController rightFilesBrowserController;

  public Button changeLanguageButton;
  public Text footerText;
  public ProgressBar deleteProgressBar;
  public Text sizeText;
  public VBox operationFlowPane;
  public Button copyButton;
  public Button cutButton;
//    public Button pasteButton;

  private OperationStorage operationStorage = null;


  @FXML
  void initialize() {
    deleteButton.setOnAction(this::onClickDeleteButton);
    copyButton.setOnAction(this::onClickCopyButton);
    cutButton.setOnAction(this::onClickCutButton);
//        pasteButton.setOnAction(this::onClickPasteButton);
    this.changeLanguageButton.setOnAction(this::onChangeLocationButtonClick);
    changeNameButton.setOnAction(this::openEditNameWindow);

    LanguageMechanics.addItem(footerText, "copyright");
    LanguageMechanics.addItem(deleteButtonTooltip, "delete");
    LanguageMechanics.addItem(changeNameButton, "changeName");
    LanguageMechanics.addItem(copyButtonTooltip, "copy");
    LanguageMechanics.addItem(cutButtonTooltip, "cut");
//        LanguageMechanics.addItem(pasteButtonTooltip, "paste");

//        pasteButton.setDisable(true);

    LanguageMechanics.updateAllItems();
//    changeLanguageButton.setText(LanguageMechanics.getLocale().getLanguage().toUpperCase());
  }

  private FilesBrowserController getSelectedFilesBrowserController() throws NullPointerException {
    if (leftFilesBrowserController.getSelectedPaths() != null) {
      return leftFilesBrowserController;
    } else if (rightFilesBrowserController.getSelectedPaths() != null) {
      return rightFilesBrowserController;
    }

    throw new NullPointerException();
  }

  //TODO to remove
  private FilesBrowserController getUnSelectedFilesBrowserController() throws NullPointerException {
    if (leftFilesBrowserController.getSelectedPaths() == null) {
      return leftFilesBrowserController;
    } else if (rightFilesBrowserController.getSelectedPaths() == null) {
      return rightFilesBrowserController;
    }
    return null;
  }


  private void onChangeLocationButtonClick(ActionEvent actionEvent) {
    this.onToogleLocation();
  }

  private void onToogleLocation() {
    if (LanguageMechanics.getLocale().toString().equals("pl")) {
      LanguageMechanics.setLocale(new Locale("en"));
      changeLanguageButton.setText("pl".toUpperCase());
    } else {
      LanguageMechanics.setLocale(new Locale("pl"));
      changeLanguageButton.setText("en".toUpperCase());
    }
  }

  private void updateAllFilesBrowsers() {
    this.leftFilesBrowserController.updateAll();
    this.rightFilesBrowserController.updateAll();
  }

  private void addOperation(Path path, MySimpleFileVisitor visitor) {
    OperationProgressController operationProgressController = new OperationProgressController(path, visitor);
    operationProgressController.addEventHandler(OperationProgressController.COMPLETED_EVENT_TYPE, event -> {
      this.updateAllFilesBrowsers();
    });
    operationFlowPane.getChildren().add(operationProgressController);
  }

  private void onClickDeleteButton(ActionEvent actionEvent) {
    Path selectedPath = null;
    try {
      selectedPath = getSelectedFilesBrowserController().getSelectedPaths();
      this.addOperation(selectedPath, new DeleteDirVisitor());
    } catch (NullPointerException e) {
      DialogUtils.showDialogNoFileSelect();
    }
  }

  private void onClickCutButton(ActionEvent actionEvent) {
    Path selectedPath = null;
    try {
      selectedPath = getSelectedFilesBrowserController().getSelectedPaths();
      Path toPath = getUnSelectedFilesBrowserController().getCurrentPath();
      this.addOperation(selectedPath, new MoveDirVisitor(selectedPath, toPath));
    } catch (NullPointerException e) {
      DialogUtils.showDialogNoFileSelect();
    }
  }

  private void onClickCopyButton(ActionEvent actionEvent) {
    Path selectedPath = null;
    try {
      selectedPath = getSelectedFilesBrowserController().getSelectedPaths();
      Path toPath = getUnSelectedFilesBrowserController().getCurrentPath();
      this.addOperation(selectedPath, new CopyDirVisitor(selectedPath, toPath));
    } catch (NullPointerException e) {
      DialogUtils.showDialogNoFileSelect();
    }
  }

  private void openEditNameWindow(ActionEvent actionEvent) {
    try {
      Path selectedPath = getSelectedFilesBrowserController().getSelectedPaths();
      File selectedFile = selectedPath.toFile();

      try {
        Stage stage = new Stage();
        NameWindowController nameWindowController = new NameWindowController(selectedFile);
        nameWindowController.addEventHandler(NameWindowController.COMPLETED_EVENT_TYPE, event -> {
          this.updateAllFilesBrowsers();
        });
        stage.setScene(new Scene(nameWindowController));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) actionEvent.getSource()).getScene().getWindow());
        stage.setTitle(LanguageMechanics.getValueOfKey("changeNameWindow"));
        stage.showAndWait();

      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (NullPointerException e) {
      DialogUtils.showDialogNoFileSelect();
    }
  }
}
