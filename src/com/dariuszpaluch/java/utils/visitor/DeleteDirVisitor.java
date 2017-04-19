package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends SimpleFileVisitor<Path> {
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
        final File currentFile = path.toFile();
        currentFile.delete();

        deletedFilesSize += Files.size(path);

        Platform.runLater(() -> {
            obsDeletedFilesSizeWrapper.set(deletedFilesSize);
        });

//        try {
//            Files.delete(path);
//        } catch (NoSuchFileException x) {
//            System.err.format("%s: no such" + " file or directory%n", path);
//        } catch (DirectoryNotEmptyException x) {
//            System.err.format("%s not empty%n", path);
//        } catch (IOException x) {
//            // File permission problems are caught here.
//            System.err.println(x);
//        }
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