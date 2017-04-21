package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class MySimpleFileVisitor extends SimpleFileVisitor<Path> {
    private long processedFilesSize = 0;
    private ReadOnlyLongWrapper obsProcessedFilesSizeWrapper = new ReadOnlyLongWrapper(0);
    private ReadOnlyLongProperty ProcessedFilesSize = obsProcessedFilesSizeWrapper.getReadOnlyProperty();

    protected void addProcessedFile(long fileSize) {
        this.addProcessedAmount(fileSize);
    }

    protected void addProcessedDir() {
        long dirSize = 1;

        this.addProcessedAmount(dirSize);
    }

    private void addProcessedAmount(long amount) {
        this.processedFilesSize += amount;

        System.out.println(this.processedFilesSize);

        Platform.runLater(() -> {
            this.obsProcessedFilesSizeWrapper.set(this.processedFilesSize);
        });
    }

    public long getProcessedFilesSize() {
        return processedFilesSize;
    }

    public ReadOnlyLongProperty getProcessedFilesSizeProperty() {
        return ProcessedFilesSize;
    }
}
