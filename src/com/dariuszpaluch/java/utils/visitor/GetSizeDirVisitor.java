package com.dariuszpaluch.java.utils.visitor;

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

public class GetSizeDirVisitor extends SimpleFileVisitor<Path> {
    private long totalSize = 0;
    private ReadOnlyLongWrapper obsTotalSizeWrapper = new ReadOnlyLongWrapper(0);
    private ReadOnlyLongProperty obsTotalSize = obsTotalSizeWrapper.getReadOnlyProperty();
    private long i = 0;

    public ReadOnlyLongProperty getObsTotalSize() {
        return obsTotalSize;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        Long fileSize = Files.size(path);
        totalSize += Files.size(path);
        i+=1;
        if(i > 500) {
            i = 0;
            obsTotalSizeWrapper.set(totalSize);

        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(exc == null){
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }

    public long getTotalSize() {
        return totalSize;
    }
}
