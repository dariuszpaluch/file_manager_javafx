package com.dariuszpaluch.java.utils.visitor;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends MySimpleFileVisitor {

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        long fileSize = Files.size(path);

        try {
            final File currentFile = path.toFile();
            currentFile.delete();
        }catch(Exception e) {
            System.out.println("Error: DeleteDirVisitor -> postVisitDirectory");
            System.out.println("ERROR" + e.getMessage());
        }finally {
            this.addProcessedFile(fileSize);
        }

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
                System.out.println("Error: DeleteDirVisitor -> postVisitDirectory");
                System.out.println(e.getMessage());
            }
            finally {
                this.addProcessedDir();
            }
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}