package com.dariuszpaluch.java.utils.tasks;

import com.dariuszpaluch.java.utils.visitor.CopyDirVisitor;
import com.dariuszpaluch.java.utils.visitor.MySimpleFileVisitor;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Task;
import sun.reflect.generics.visitor.Visitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class WalkFileTreeTheadTask extends Task {
    private Path path;
    private MySimpleFileVisitor visitor;
    private ReadOnlyLongProperty obsProcessedFilesSize;

    public WalkFileTreeTheadTask(Path path, MySimpleFileVisitor visitor) {
        this.path = path;
        this.visitor = visitor;
    }

    @Override
    protected Object call() throws Exception {
        if(Files.isDirectory(path)) {
            Files.walkFileTree(path, visitor);
        }
        else {
            visitor.visitFile(path, null);
        }
        return visitor.getProcessedFilesSize();
    }
}
