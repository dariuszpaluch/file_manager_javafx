package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.tasks.WalkFileTreeTheadTask;
import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;
import com.dariuszpaluch.java.utils.visitor.MySimpleFileVisitor;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.value.ObservableLongValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Path;

public class OperationProgressController extends FlowPane {
    private Path path;
    public ProgressBar progressBar;
    public Button cancelButton;
    public Text sizeTitleText;
    public Text totalSizeText;
    public Text removingFilesText;
    public Text removingPathText;

    private ReadOnlyLongProperty operationFilesSize;
    private ReadOnlyLongProperty processedFilesSizeProperty;

    private MySimpleFileVisitor operationSimpleFileVisitor;

    private WalkFileTreeTheadTask getSizeTreeTheadTask;
    private WalkFileTreeTheadTask operationTreeTheadTask;

    public OperationProgressController(Path path, MySimpleFileVisitor operationSimpleFileVisitor) {
        this.path = path;

        this.getSizeTreeTheadTask = new WalkFileTreeTheadTask(this.path, new GetSizeDirVisitor());
        this.operationFilesSize = getSizeTreeTheadTask.getObsProcessedFilesSize();
        this.operationSimpleFileVisitor = operationSimpleFileVisitor;
        this.operationTreeTheadTask = new WalkFileTreeTheadTask(this.path, operationSimpleFileVisitor);

        this.processedFilesSizeProperty = this.operationSimpleFileVisitor.getObsProcessedFilesSize();

        getSizeTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                onGetTotalSizeSuccess();
            }
        });

        operationTreeTheadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                onOperationSuccess();
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

    @FXML
    void initialize() {
        LanguageMechanics.addItem(cancelButton, "cancel");
        LanguageMechanics.addItem(removingFilesText, "deleting");
        LanguageMechanics.addItem(sizeTitleText, "size");

        removingPathText.setText(this.path.toString());
        progressBar.setProgress(-1.0);
        totalSizeText.textProperty().bind(operationFilesSize.asString());
        new Thread(getSizeTreeTheadTask).start();
    }

    private static NumberBinding calculateProgress(final ObservableLongValue totalSize, final ObservableLongValue progressSize) {
        return new DoubleBinding() {
            {
                super.bind(totalSize, progressSize);
            }

            @Override
            protected double computeValue() {
                if (totalSize.doubleValue() > 0 && progressSize.doubleValue() > 0) {
                    return progressSize.doubleValue() / totalSize.doubleValue();
                }

                return -1.0;
            }
        };
    }
    private void onGetTotalSizeSuccess() {
        new Thread(this.operationTreeTheadTask).start();
        Long result = (Long) getSizeTreeTheadTask.getValue();
        totalSizeText.setText(Long.toString(result));

        progressBar.progressProperty().bind(calculateProgress(this.operationFilesSize, this.processedFilesSizeProperty));
    }

    private void onOperationSuccess() {
//        System.out.println(Long.toString((Long) deleteTreeTheadTask.getValue()));
    }

}
