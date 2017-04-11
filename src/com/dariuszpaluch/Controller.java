package com.dariuszpaluch;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class Controller {
    public Button deleteButton;
    public Button changeNameButton;

    @FXML
    public FilesBrowserController leftFilesBrowserController;
    @FXML
    public FilesBrowserController rightFilesBrowserController;

    @FXML
    void initialize() {
        deleteButton.setOnAction(this::onClickDeleteButton);
    }

    private void onClickDeleteButton(ActionEvent actionEvent) {
//        String name = leftFilesListView.getSelectionModel().getSelectedItem();
//        File selectedFile = new File(this.currentPath + File.separator + name);
//        selectedFile.delete();
//        readAllFilesInFolder(this.currentPath);
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
