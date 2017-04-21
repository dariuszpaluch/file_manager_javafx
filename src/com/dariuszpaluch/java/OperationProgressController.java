package com.dariuszpaluch.java;

import com.dariuszpaluch.java.utils.tasks.WalkFileTreeTheadTask;
import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;
import com.dariuszpaluch.java.utils.visitor.MySimpleFileVisitor;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.value.ObservableLongValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Path;

public class OperationProgressController extends VBox {
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
    public class CompletedEvent extends Event {

        public CompletedEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
            super(eventType);
        }
    }
    public static EventType<Event> COMPLETED_EVENT_TYPE = new EventType<>("COMPLETED");
    Event completedEvent = new CompletedEvent(COMPLETED_EVENT_TYPE);

    public OperationProgressController(Path path, MySimpleFileVisitor operationSimpleFileVisitor) {
        this.path = path;

        MySimpleFileVisitor getSizeDirVisitor = new GetSizeDirVisitor();
        this.getSizeTreeTheadTask = new WalkFileTreeTheadTask(this.path, getSizeDirVisitor);
        this.operationFilesSize = getSizeDirVisitor.getProcessedFilesSizeProperty();
        this.operationSimpleFileVisitor = operationSimpleFileVisitor;
        this.operationTreeTheadTask = new WalkFileTreeTheadTask(this.path, operationSimpleFileVisitor);

        this.processedFilesSizeProperty = this.operationSimpleFileVisitor.getProcessedFilesSizeProperty();

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
        Platform.runLater(() -> {
            totalSizeText.setText(Long.toString(result));
        });

        progressBar.progressProperty().bind(calculateProgress(this.operationFilesSize, this.processedFilesSizeProperty));
    }

    private void onOperationSuccess() {
        cancelButton.setVisible(false);
//        System.out.println(Long.toString((Long) deleteTreeTheadTask.getValue()));
        this.fireEvent(completedEvent);
    }

}
