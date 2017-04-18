package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends SimpleFileVisitor<Path> {
    private int i = 0;
    private long deletedFilesSize = 0;
    private ReadOnlyLongWrapper obsDeletedFilesSizeWrapper = new ReadOnlyLongWrapper(0);
    private ReadOnlyLongProperty obsDeletedFilesSize = obsDeletedFilesSizeWrapper.getReadOnlyProperty();

    public long getDeletedFilesSize() {
        return deletedFilesSize;
    }

    public ReadOnlyLongProperty getObsDeletedFilesSize() {
        return obsDeletedFilesSize;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        deletedFilesSize += Files.size(path);

        Platform.runLater(() -> {
            obsDeletedFilesSizeWrapper.set(deletedFilesSize);
        });
        try {
            Files.delete(path);
        }
        catch(Exception e) {
            System.out.println("ERRROR");
            System.out.println(e.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(exc == null){
            try {
                Files.delete(dir);
            }
            catch(Exception e) {
                System.out.println("ERRROR");

                System.out.println(e.getMessage());
            }
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}