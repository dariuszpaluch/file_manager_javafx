package com.dariuszpaluch.java.utils.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends SimpleFileVisitor<Path> {
    private long copiedSize = 0;
    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

        copiedSize += Files.size(path);

        Files.delete(path);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(exc == null){
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
        throw exc;
    }
}
