package com.dariuszpaluch.java.visitors;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends MySimpleFileVisitor {

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }

    long fileSize = Files.size(path);

    try {
      final File currentFile = path.toFile();
      currentFile.delete();
    } catch (Exception e) {
      System.out.println("Error: DeleteDirVisitor -> postVisitDirectory");
      System.out.println("ERROR" + e.getMessage());
    } finally {
      this.addProcessedFile(fileSize);
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    if (this.stop) {
      return FileVisitResult.TERMINATE;
    }

    if (exc == null) {
      try {
        Files.delete(dir);
      } catch (Exception e) {
        System.out.println("Error: DeleteDirVisitor -> postVisitDirectory");
        System.out.println(e.getMessage());
      } finally {
        this.addProcessedDir();
      }
      return FileVisitResult.CONTINUE;
    }
    throw exc;
  }
}