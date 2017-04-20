package com.dariuszpaluch.java.utils.visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class GetSizeDirVisitor extends MySimpleFileVisitor {

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        long fileSize = Files.size(path);

        this.addProcessedFile(fileSize);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        this.addProcessedDir();

        if(exc == null){
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}
