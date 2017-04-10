package com.dariuszpaluch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    public Button refreshButton;
    @FXML
    Button changeNameButton;
    @FXML
    Text currentPathText;
    @FXML
    Button loadButton;
    @FXML
    Button goUpButton;

    @FXML
    ListView<String> filesListView;

    Path currentPath;
    ObservableList<String> curredFolderList;

    @FXML
    void initialize() {
        loadButton.setOnAction(this::onClickLoadButton);
        goUpButton.setOnAction(this::onClickGoUpButton);
        changeNameButton.setOnAction(this::onClickChangeNameButton);
        refreshButton.setOnAction(this::onClickRefreshButton);

        this.curredFolderList = FXCollections.observableArrayList();
        filesListView.setItems(this.curredFolderList);
        filesListView.setOnMouseClicked(this::onListItemDoubleClick);

        this.openCurrectDir();
    }

    private void onClickRefreshButton(ActionEvent actionEvent) {
        this.readAllFilesInFolder(this.currentPath);
    }

    private void onClickChangeNameButton(ActionEvent actionEvent) {
//        this.curredFolderList.get
        this.openEditNameWindow(actionEvent);

    }

    private void openEditNameWindow(ActionEvent actionEvent) {
        String name = filesListView.getSelectionModel().getSelectedItem();
        File selectedFile = new File(this.currentPath + File.separator + name);

        if(name != null ) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("name_window.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(
                        ((Node)actionEvent.getSource()).getScene().getWindow() );
                NameWindowController controller = fxmlLoader.<NameWindowController>getController();
//                controller.setName(name);
                controller.setOldFile(selectedFile);
//                stage.setOnCloseRequest(event -> readAllFilesInFolder(currentPath));
                stage.setTitle("Change file name");
                stage.showAndWait();
                stage.setOnCloseRequest(event -> {
                    System.out.println("View  got close request. Notifying callback");
//                    onCloseCallback.get().invoke();
                });
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onClickLoadButton(ActionEvent event) {
        this.openCurrectDir();
    }

    @FXML
    public void onClickGoUpButton(ActionEvent event) {
        this.goUpDir();
    }

    private void openCurrectDir() {
        this.currentPath = Paths.get(System.getProperty("user.dir"));
        this.readAllFilesInFolder(this.currentPath);
    }

    private void onListItemDoubleClick(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
            String selectedDir = filesListView.getSelectionModel().getSelectedItem();
            this.goToNextDir(selectedDir);
        }
    }

    private void goToNextDir(String dir) {
        Path tempPath = this.currentPath.resolve(dir);
        if(isDir(tempPath)) {
            this.currentPath = tempPath;
            readAllFilesInFolder(this.currentPath);
            goUpButton.setDisable(false);
        }
    }

    private void goUpDir() {
        this.currentPath =  this.currentPath.getParent();
        readAllFilesInFolder(this.currentPath);

        if(this.currentPath.getParent() == null) {
            goUpButton.setDisable(true);
        }
    }

    private boolean isDir(Path path) {
        if (path == null || !Files.exists(path)) return false;
        else return Files.isDirectory(path);
    }

    private void updatePathText() {
        this.currentPathText.setText(this.currentPath.toString());
    }

    public void readAllFilesInFolder(final Path path) {
        System.out.println(path);
        this.updatePathText();
        this.curredFolderList.clear();

        if(isDir(path)) {
            final File folder = path.toFile();
            for (final File fileEntry : folder.listFiles()) {
                String fileName = fileEntry.getName();
                if (fileEntry.isDirectory()) {
                    this.curredFolderList.add(fileName);
                } else {
                    this.curredFolderList.add(fileName);
                }
            }
        }

    }
}
