package com.dariuszpaluch.java.utils.visitor;

import javafx.beans.property.ReadOnlyLongProperty;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public abstract class MySimpleFileVisitor extends SimpleFileVisitor<Path> {
    public abstract long getProcessedFilesSize();
    public abstract ReadOnlyLongProperty  getObsProcessedFilesSize();
}
