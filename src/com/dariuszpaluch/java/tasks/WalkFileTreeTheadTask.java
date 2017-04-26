package com.dariuszpaluch.java.tasks;

import com.dariuszpaluch.java.visitors.MySimpleFileVisitor;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.nio.file.Files;
import java.nio.file.Path;

public class WalkFileTreeTheadTask extends Service {
    private Path path;
    private MySimpleFileVisitor visitor;
    private ReadOnlyLongProperty obsProcessedFilesSize;

    public WalkFileTreeTheadTask(Path path, MySimpleFileVisitor visitor) {
        this.path = path;
        this.visitor = visitor;

        this.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                visitor.stop = true;
            }
        });
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(Files.isDirectory(path)) {

                    Files.walkFileTree(path, visitor);
                }
                else {
                    visitor.visitFile(path, null);
                }
                return visitor.getProcessedFilesSize();
            }
        };
    }
}
