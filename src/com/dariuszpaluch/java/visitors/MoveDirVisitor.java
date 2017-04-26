package com.dariuszpaluch.java.visitors;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class MoveDirVisitor extends CopyDirVisitor {

  public MoveDirVisitor(Path fromPath, Path toPath) {
    super(fromPath, toPath);
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }

    FileVisitResult result = super.visitFile(path, attrs);

    final File currentFile = path.toFile();
    currentFile.delete();

    return result;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }

    final File currentFile = dir.toFile();
    currentFile.delete();

    return FileVisitResult.CONTINUE;
  }
}
