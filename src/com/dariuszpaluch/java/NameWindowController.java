package com.dariuszpaluch.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class NameWindowController {
    public Button cancelButton;
    public Button saveButton;
    public TextField nameTextField;
    public Text errorText;

    //    private String name = "";
    private File oldFile;

    @FXML
    void initialize() {
        this.nameTextField.setText("");
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

        if (newFile.exists()) {
            errorText.setText("File with this name is exist");
        }
        else {
            boolean success = this.oldFile.renameTo(newFile);
            if(success) {

                final Node source = (Node) actionEvent.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                stage.close();

                System.out.println("CLOSE");
            }
        }
        System.out.println("SAVE");
    }


//    public String getName() {
//        return name;
//    }

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
