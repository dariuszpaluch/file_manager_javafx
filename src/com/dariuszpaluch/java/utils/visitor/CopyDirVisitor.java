package com.dariuszpaluch.java.utils.visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirVisitor extends MySimpleFileVisitor {
    private Path fromPath;
    private Path toPath;

    public CopyDirVisitor(Path fromPath, Path toPath) {
        this.fromPath = fromPath;

        this.toPath = toPath.resolve(fromPath.getFileName());

//        try {
//            Files.createDirectory(toPath.resolve(fromPath.relativize(this.fromPath)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }

    private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path relativizePath = fromPath.relativize(dir);
        System.out.println("dir" + dir);
        System.out.println("RELATIVIZE" + fromPath.relativize(dir));
        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        System.out.println("target" + targetPath);

        if (!Files.exists(targetPath)) {
            System.out.println("CREATE");
            Files.createDirectory(targetPath);
            this.addProcessedDir();

        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println("dir" + file);
        System.out.println("RELATIVIZE" + fromPath.relativize(file));
        System.out.println("target" + toPath.resolve(fromPath.relativize(file)));

        System.out.println(file);
        long fileSize = Files.size(file);
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        System.out.println("file" + toPath.resolve(fromPath.relativize(file)));

        this.addProcessedFile(fileSize);
        return FileVisitResult.CONTINUE;
    }
}
