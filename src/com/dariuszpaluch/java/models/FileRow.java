package com.dariuszpaluch.java.models;

import com.dariuszpaluch.java.utils.DateUtils;
import com.dariuszpaluch.java.utils.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileRow {

  private final String name;
//  private final String ext;
  private final String size;
  private final String date;
  private final String attr;
  private final Path path;

  public FileRow(File file) {
    Path path = Paths.get(file.getPath());

    String name = file.getName();
//    String ext = FileUtils.getExtension(file);
    String size = file.isDirectory() ? "<DIR>" : Long.toString(file.length());
    String date = DateUtils.getStringDateWithTime(file.lastModified());
    String attr = FileUtils.getPermissionsToString(file);


    this.name = name;
//    this.ext = ext;
    this.size = size;
    this.date = date;
    this.attr = attr;
    this.path = path;
  }


  public FileRow(String name, String ext, Path path, String size, String date, String attr) {
    this.name = name;
//    this.ext = ext;
    this.size = size;
    this.date = date;
    this.attr = attr;
    this.path = path;

  }

  public String getName() {
    return name;
  }

//  public String getExt() {
//    return ext;
//  }

  public String getSize() {
    return size;
  }

  public String getDate() {
    return date;
  }

  public String getAttr() {
    return attr;
  }

  public Path getPath() {
    return path;
  }
}
