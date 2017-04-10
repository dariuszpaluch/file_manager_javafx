package com.dariuszpaluch;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class FileRow {

    private final String name;
    private final String ext;
    private final String size;
    private final String date;
    private final String attr;


    public FileRow(File file) {
        String name = file.getName();
        String ext = FileUtils.getExtension(file);
        String size = file.isDirectory() ? "<DIR>" : Long.toString(file.length());
        String date = DateUtils.getStringDateWithTime(file.lastModified());
        String attr = ""; //TODO Get rwx attributes from file

        this.name = name;
        this.ext = ext;
        this.size = size;
        this.date = date;
        this.attr = attr;
    }


    public FileRow(String name, String ext, String path, String size, String date, String attr) {
        this.name = name;
        this.ext = ext;
        this.size = size;
        this.date = date;
        this.attr = attr;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getAttr() {
        return attr;
    }
}
