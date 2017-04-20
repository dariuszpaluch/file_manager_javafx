package com.dariuszpaluch.java.utils.visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirVisitor extends MySimpleFileVisitor {
    private Path fromPath;
    private Path toPath;

    public CopyDirVisitor(Path fromPath, Path toPath) {
        this.fromPath = fromPath;
        this.toPath = toPath;
    }

    private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        this.addProcessedDir();

        if (!Files.exists(targetPath)) {
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        long fileSize = Files.size(file);
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);

        this.addProcessedFile(fileSize);
        return FileVisitResult.CONTINUE;
    }
}
