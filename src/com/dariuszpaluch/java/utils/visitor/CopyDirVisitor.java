package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirVisitor extends MySimpleFileVisitor {
    private Path fromPath;
    private Path toPath;
    private long copiedFilesSize = 0;
    private ReadOnlyLongWrapper obsCopiedFilesSizeWrapper = new ReadOnlyLongWrapper(0);
    private ReadOnlyLongProperty obsCopiedFilesSize = obsCopiedFilesSizeWrapper.getReadOnlyProperty();


    public CopyDirVisitor(Path fromPath, Path toPath) {
        this.fromPath = fromPath;
        this.toPath = toPath;
    }

    @Override
    public long getProcessedFilesSize() {
        return copiedFilesSize;
    }

    @Override
    public ReadOnlyLongProperty getObsProcessedFilesSize() {
        return obsCopiedFilesSize;
    }

    private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetPath = toPath.resolve(fromPath.relativize(dir));

        copiedFilesSize += Files.size(targetPath);
        Platform.runLater(() -> {
            obsCopiedFilesSizeWrapper.set(copiedFilesSize);
        });

        if(!Files.exists(targetPath)){
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        return FileVisitResult.CONTINUE;
    }


}
