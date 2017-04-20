package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.DirUtils;
import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Controller {
    public Button deleteButton;
    public Button changeNameButton;

    @FXML
    public FilesBrowserController leftFilesBrowserController;
    @FXML
    public FilesBrowserController rightFilesBrowserController;
    public Button changeLanguageButton;
    public Text footerText;
    public ProgressBar deleteProgressBar;
    public Text sizeText;
    public FlowPane operationFlowPane;
    public Button copyButton;
    public Button cutButton;
    public Button pasteButton;

    @FXML
    void initialize() {
        deleteButton.setOnAction(this::onClickDeleteButton);
        copyButton.setOnAction(this::onClickCopyButton);
        cutButton.setOnAction(this::onClickCutButton);
        pasteButton.setOnAction(this::onClickPasteButton);
        this.changeLanguageButton.setOnAction(this::onChangeLocationButtonClick);

        LanguageMechanics.addItem(footerText, "copyright");
        LanguageMechanics.addItem(deleteButton, "delete");
        LanguageMechanics.addItem(changeNameButton, "changeName");
        LanguageMechanics.addItem(copyButton, "copy");
        LanguageMechanics.addItem(cutButton, "cut");
        LanguageMechanics.addItem(pasteButton, "paste");

        pasteButton.setDisable(true);

        LanguageMechanics.updateAllItems();
        changeLanguageButton.setText(LanguageMechanics.getLocale().getLanguage().toUpperCase());


    }



    private void onChangeLocationButtonClick(ActionEvent actionEvent) {
        this.onToogleLocation();
    }

    private void onToogleLocation() {
        if(LanguageMechanics.getLocale().toString().equals("pl")) {
            LanguageMechanics.setLocale(new Locale("en"));
        }
        else {
            LanguageMechanics.setLocale(new Locale("pl"));
        }

        changeLanguageButton.setText(LanguageMechanics.getLocale().getLanguage().toUpperCase());
    }
    private void onClickDeleteButton(ActionEvent actionEvent) {
        Path path = leftFilesBrowserController.getSelectedPaths();

        operationFlowPane.getChildren().add(new OperationProgressController(path));
    }

    private void onClickPasteButton(ActionEvent actionEvent) {
        pasteButton.setDisable(true);
    }

    private void onClickCutButton(ActionEvent actionEvent) {
        pasteButton.setDisable(false);
    }

    private void onClickCopyButton(ActionEvent actionEvent) {
        pasteButton.setDisable(false);
    }

//    private void onClickChangeNameButton(ActionEvent actionEvent) {
////        this.curredFolderList.get
//        this.openEditNameWindow(actionEvent);
//
//    }

//    private void openEditNameWindow(ActionEvent actionEvent) {
//        String name = leftFileRows.get(leftFilesTableView.getSelectionModel().getFocusedIndex()).getName();
//        File selectedFile = new File(this.currentPath + File.separator + name);
//
//        if(name != null ) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/name_window.fxml"));
//                Parent root = (Parent) fxmlLoader.load();
//                Stage stage = new Stage();
//                stage.setScene(new Scene(root));
//                stage.initModality(Modality.WINDOW_MODAL);
//                stage.initOwner(
//                        ((Node)actionEvent.getSource()).getScene().getWindow() );
//                NameWindowController controller = fxmlLoader.<NameWindowController>getController();
////                controller.setName(name);
//                controller.setOldFile(selectedFile);
////                stage.setOnCloseRequest(event -> readAllFilesInFolder(currentPath));
//                stage.setTitle("Change file name");
//                stage.showAndWait();
//                stage.setOnCloseRequest(event -> {
//                    System.out.println("View  got close request. Notifying callback");
////                    onCloseCallback.get().invoke();
//                });
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
