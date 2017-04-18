package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.tasks.DeleteTreeTheadTask;
import com.dariuszpaluch.java.utils.tasks.GetSizeTreeTheadTask;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
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
    private Path path;
    public ProgressBar progressBar;
    public ProgressIndicator progressIndicator;
    public Button cancelButton;
    public Text totalSizeText;
    public Text removingFilesText;
    public Text removingPathText;
    public Text sizeTitleText;
    public Text sizeText;

    private ReadOnlyLongProperty operationFilesSize;
    private ReadOnlyDoubleProperty obsProgressProperty;
    private ReadOnlyLongProperty deletedFilesSize;

    private GetSizeTreeTheadTask getSizeTreeTheadTask;
    private DeleteTreeTheadTask deleteTreeTheadTask;

    public OperationProgressController(Path path){
        this.path = path;
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

        deleteTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                onDeleteSuccess();
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
                super.bind(totalSize, progressSize);
            }
            @Override protected double computeValue() {
//                System.out.println(Double.toString(totalSize.doubleValue()));
//                System.out.println(Double.toString(progressSize.doubleValue()));
                if(totalSize.doubleValue() > 0 && progressSize.doubleValue() > 0) {
//                    System.out.println("wynik" + Double.toString(progressSize.doubleValue() / totalSize.doubleValue()));
                 return progressSize.doubleValue() / totalSize.doubleValue();
                }

                return -1.0;
            }
        };
    }
    @FXML
    void initialize() {
        LanguageMechanics.addItem(cancelButton, "cancel");
//        removingFilesText
        LanguageMechanics.addItem(removingFilesText, "deleting");
        LanguageMechanics.addItem(sizeTitleText, "size");

        removingPathText.setText(this.path.toString());
////        progressPropertyWrapper.set();
        progressBar.setProgress(-1.0); //to show prepare loading
        progressIndicator.setProgress(-1.0);
        totalSizeText.textProperty().bind(operationFilesSize.asString());
//        getSize
        new Thread(getSizeTreeTheadTask).start();
////        progressBar.
    }

    private void onGetTotalSizeSuccess() {
        new Thread(deleteTreeTheadTask).start();
        Long result = (Long)getSizeTreeTheadTask.getValue();

        sizeText.setText(Long.toString(result));

        progressBar.progressProperty().bind(calculateProgress(operationFilesSize, deletedFilesSize));
        progressIndicator.progressProperty().bind(calculateProgress(operationFilesSize, deletedFilesSize));

        totalSizeText.textProperty().bind(calculateProgress(operationFilesSize, deletedFilesSize).asString());
    }

    private void onDeleteSuccess() {
        System.out.println(Long.toString((Long)deleteTreeTheadTask.getValue()));
    }

//    private void start() {
//        new Thread(getSizeTreeTheadTask).start();
//    }

}
