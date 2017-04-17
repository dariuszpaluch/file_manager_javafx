package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.tasks.GetSizeTreeTheadTask;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Path;

public class OperationProgressController extends FlowPane {
    public ProgressBar progressBar;
    public ProgressIndicator progressIndicator;
    public Button cancelButton;
    public Text totalSizeText;

    private ReadOnlyLongProperty operationFilesSize;
    private ReadOnlyDoubleWrapper progressPropertyWrapper;
    private ReadOnlyLongProperty filesSizeDone = new ReadOnlyLongWrapper(50).getReadOnlyProperty();
    private GetSizeTreeTheadTask getSizeTreeTheadTask;

    public OperationProgressController(Path path)  {
        super();
        getSizeTreeTheadTask = new GetSizeTreeTheadTask(path);
        operationFilesSize = getSizeTreeTheadTask.getObsTotalSize();

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
//        System.out.println("INITIALIZE");

////        progressPropertyWrapper.set();
        progressBar.setProgress(-1.0); //to show prepare loading
        progressIndicator.setProgress(-1.0);
        totalSizeText.textProperty().bind(operationFilesSize.asString());
        new Thread(getSizeTreeTheadTask).start();
////        progressBar.
    }

//    private void start() {
//        new Thread(getSizeTreeTheadTask).start();
//    }

}
