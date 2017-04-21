package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.visitor.CopyDirVisitor;
import com.dariuszpaluch.java.utils.visitor.DeleteDirVisitor;
import com.dariuszpaluch.java.utils.visitor.MySimpleFileVisitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.Locale;

public class Controller {
    public Tooltip pasteButtonTooltip;
    public Tooltip cutButtonTooltip;
    public Tooltip deleteButtonTooltip;
    public Tooltip copyButtonTooltip;

    enum OperationTypeEnum {
        COPIE,
        MOVE
    };

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
    public Button pasteButton;

    private OperationStorage operationStorage = null;


    @FXML
    void initialize() {
        deleteButton.setOnAction(this::onClickDeleteButton);
        copyButton.setOnAction(this::onClickCopyButton);
        cutButton.setOnAction(this::onClickCutButton);
        pasteButton.setOnAction(this::onClickPasteButton);
        this.changeLanguageButton.setOnAction(this::onChangeLocationButtonClick);

        LanguageMechanics.addItem(footerText, "copyright");
        LanguageMechanics.addItem(deleteButtonTooltip, "delete");
        LanguageMechanics.addItem(changeNameButton, "changeName");
        LanguageMechanics.addItem(copyButtonTooltip, "copy");
        LanguageMechanics.addItem(cutButtonTooltip, "cut");
        LanguageMechanics.addItem(pasteButtonTooltip, "paste");

        pasteButton.setDisable(true);

        LanguageMechanics.updateAllItems();
        changeLanguageButton.setText(LanguageMechanics.getLocale().getLanguage().toUpperCase());
    }

    private FilesBrowserController getSelectedFilesBrowserController() {
        if(leftFilesBrowserController.getSelectedPaths() != null) {
            return leftFilesBrowserController;
        }
        else {
            return rightFilesBrowserController;
        }
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
        Path path = getSelectedFilesBrowserController().getSelectedPaths();
        this.addOperation(path, new DeleteDirVisitor());
    }

    private void onClickPasteButton(ActionEvent actionEvent) {
        pasteButton.setDisable(true);

        Path toPath = getSelectedFilesBrowserController().getSelectedPaths().getParent();
        Path fromPath = this.operationStorage.getPath();
        this.addOperation(fromPath, new CopyDirVisitor(fromPath, toPath));
    }

    private void onClickCutButton(ActionEvent actionEvent) {
        Path path = getSelectedFilesBrowserController().getSelectedPaths();

        this.operationStorage = new OperationStorage(path, OperationTypeEnum.MOVE);

        pasteButton.setDisable(false);
    }

    private void onClickCopyButton(ActionEvent actionEvent) {
        Path path = getSelectedFilesBrowserController().getSelectedPaths();

        this.operationStorage = new OperationStorage(path, OperationTypeEnum.COPIE);
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
