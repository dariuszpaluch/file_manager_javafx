package com.dariuszpaluch.java.utils.tasks;

import com.dariuszpaluch.java.utils.visitor.DeleteDirVisitor;
import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Task;

import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteTreeTheadTask extends Task {
    private Path path;
    private DeleteDirVisitor visitor = new DeleteDirVisitor();
    private ReadOnlyLongProperty obsDeletedFilesSize = visitor.getObsDeletedFilesSize();


    public DeleteTreeTheadTask(Path path) {
        this.path = path;
    }

    public ReadOnlyLongProperty getObsDeletedFilesSize() {
        return obsDeletedFilesSize;
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("Path" + path);
        Files.walkFileTree(path, visitor);
        return visitor.getDeletedFilesSize();
    }
}
