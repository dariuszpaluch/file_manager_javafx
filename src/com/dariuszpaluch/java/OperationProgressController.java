package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.tasks.DeleteTreeTheadTask;
import com.dariuszpaluch.java.utils.tasks.GetSizeTreeTheadTask;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.value.ObservableLongValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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
    private ReadOnlyDoubleProperty obsProgressProperty;
    private ReadOnlyLongProperty deletedFilesSize;

    private GetSizeTreeTheadTask getSizeTreeTheadTask;
    private DeleteTreeTheadTask deleteTreeTheadTask;

    public OperationProgressController(Path path)  {
        super();
        getSizeTreeTheadTask = new GetSizeTreeTheadTask(path);
        operationFilesSize = getSizeTreeTheadTask.getObsTotalSize();
        deleteTreeTheadTask = new DeleteTreeTheadTask(path);
        deletedFilesSize = deleteTreeTheadTask.getObsDeletedFilesSize();

//        obsProgressProperty = Bindings.divide(deletedFilesSize, operationFilesSize);


        getSizeTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                onGetTotalSizeSuccess();
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

    private static NumberBinding calculateProgress(final ObservableLongValue totalSize, final ObservableLongValue progressSize) {
        return new DoubleBinding() {
            {
                bind(totalSize, progressSize);
            }
            @Override protected double computeValue() {
                System.out.println(Double.toString(totalSize.doubleValue()));
                System.out.println(Double.toString(progressSize.doubleValue()));
                if(totalSize.doubleValue() > 0 && progressSize.doubleValue() > 0) {
                 return progressSize.doubleValue() / totalSize.doubleValue();
                }

                return -1.0;
            }
        };
    }
    @FXML
    void initialize() {
        System.out.println("INITIALIZE");

////        progressPropertyWrapper.set();
        progressBar.setProgress(-1.0); //to show prepare loading
        progressIndicator.setProgress(-1.0);
        totalSizeText.textProperty().bind(operationFilesSize.asString());
//        getSize
        new Thread(getSizeTreeTheadTask).start();
////        progressBar.
    }

    private void onGetTotalSizeSuccess() {
        System.out.println("SUCCESS");
        Long result = (Long)getSizeTreeTheadTask.getValue();
        new Thread(deleteTreeTheadTask).start();
        progressBar.progressProperty().bind(calculateProgress(operationFilesSize, deletedFilesSize));
        progressIndicator.progressProperty().bind(calculateProgress(operationFilesSize, deletedFilesSize));
    }

//    private void start() {
//        new Thread(getSizeTreeTheadTask).start();
//    }

}
