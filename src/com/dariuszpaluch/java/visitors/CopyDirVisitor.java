package com.dariuszpaluch.java.visitors;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirVisitor extends MySimpleFileVisitor {
  private Path fromPath;
  private Path toPath;

  public CopyDirVisitor(Path fromPath, Path toPath) {
    this.fromPath = fromPath;

    //copy to dir with this same name
    this.toPath = toPath.resolve(fromPath.getFileName());
  }

  private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }
    Path relativizePath = fromPath.relativize(dir);
    Path targetPath = toPath.resolve(fromPath.relativize(dir));

    if (!Files.exists(targetPath)) {
      Files.createDirectory(targetPath);
      this.addProcessedDir();
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }

    long fileSize = Files.size(file);
    Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);

    this.addProcessedFile(fileSize);
    return FileVisitResult.CONTINUE;
  }
}
