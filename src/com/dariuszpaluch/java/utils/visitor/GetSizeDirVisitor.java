package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableLongValue;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Delayed;

public class GetSizeDirVisitor extends MySimpleFileVisitor {
    private long totalSize = 0;
    private ReadOnlyLongWrapper obsTotalSizeWrapper = new ReadOnlyLongWrapper(0);
    private ReadOnlyLongProperty obsTotalSize = obsTotalSizeWrapper.getReadOnlyProperty();

    @Override
    public long getProcessedFilesSize() {
        return this.totalSize;
    }

    @Override
    public ReadOnlyLongProperty getObsProcessedFilesSize() {
        return this.obsTotalSize;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        Long fileSize = Files.size(path);
        totalSize += Files.size(path);

        Platform.runLater(() -> {
            obsTotalSizeWrapper.set(totalSize);
        });
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(exc == null){
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}
