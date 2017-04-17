package com.dariuszpaluch.java.utils.tasks;

import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Task;

import java.nio.file.Files;
import java.nio.file.Path;

public class GetSizeTreeTheadTask extends Task {
    private Path path;
    private double totalSize;
    private ReadOnlyLongProperty obsTotalSize;
    private GetSizeDirVisitor visitor = new GetSizeDirVisitor();

    public GetSizeTreeTheadTask(Path path) {
        this.path = path;
        obsTotalSize = visitor.getObsTotalSize();
    }

    public ReadOnlyLongProperty getObsTotalSize() {
        return obsTotalSize;
    }

    @Override
    protected Object call() throws Exception {
        Files.walkFileTree(path, visitor);
        return visitor.getTotalSize();
    }
}
