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

    @FXML
    void initialize() {
        deleteButton.setOnAction(this::onClickDeleteButton);
        this.changeLanguageButton.setOnAction(this::onChangeLocationButtonClick);

        LanguageMechanics.addItem(footerText, "copyright");
        LanguageMechanics.addItem(deleteButton, "delete");
        LanguageMechanics.addItem(changeNameButton, "changeName");
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

    public class GetSizeTheadTask extends Task {
        private double totalSize = 0.0;
        private Path path;
        private ReadOnlyLongProperty obsTotalSize;
        private GetSizeDirVisitor visitor = new GetSizeDirVisitor();

        public GetSizeTheadTask(Path path) {
            this.path = path;
            obsTotalSize = visitor.getObsTotalSize();
        }

        public double getTotalSize() {
            return totalSize;
        }

        public ReadOnlyLongProperty getObsTotalSize() {
            return obsTotalSize;
        }

        @Override
        protected Object call() throws Exception {
            Files.walkFileTree(path, visitor);
            return visitor.getTotalSize();
//            DirUtils.getTotalSize(this.path);
        }
    }


    private void onClickDeleteButton(ActionEvent actionEvent) {
        Path path = leftFilesBrowserController.getSelectedPaths();
//        try {
//            Pane pane = FXMLLoader.load(getClass().getResource("../resources/fxml/operation_progress.fxml"));
//            operationFlowPane.getChildren().add(pane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        };

//        operationFlowPane.getChildren().add(new Button ("DAREK"));
        operationFlowPane.getChildren().add(new OperationProgressController(path));
//        try {
//            DirUtils.deleteIfExists(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        GetSizeTheadTask task = new GetSizeTheadTask(path); //1
////        GetSizeDirVisitor visitor = new GetSizeDirVisitor(path);
//        sizeText.textProperty().bind(task.getObsTotalSize().asString() );
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent t) {
//                System.out.println("FINISH");
//                System.out.println(Long.toString((Long)task.getValue()));
//            }
//        });
//        new Thread(task).start(); //2



//        try {
//            System.out.println(DirUtils.getTotalSize(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
