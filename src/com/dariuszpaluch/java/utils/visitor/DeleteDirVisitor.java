package com.dariuszpaluch.java.utils.visitor;

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
        System.out.println("TUTAJ 2" + path);

        i +=1;
            System.out.println("i" + i);
        if(i > 100) {
            obsDeletedFilesSizeWrapper.set(deletedFilesSize);
            i = 0;
        }
        deletedFilesSize += Files.size(path);

        Files.delete(path);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("TUTAJ" + dir);
        if(exc == null){
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}
