package com.dariuszpaluch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    static public String getExtension(File file) {
        String extension = "";
        String fileName = file.getName();

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    };

    static public String getLastModifiedTime(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(file.lastModified());
    }

    static public String getPermissionsToString(File file) {
        String attr = "";
        attr += file.canRead() ? "r" : "-";
        attr += file.canWrite() ? "w" : "-";
        attr += file.canExecute()? "x" : "-";

        return attr;
    }

    static public boolean isDir(Path path) {
        return !(path == null || !Files.exists(path)) && Files.isDirectory(path);
    }

    static public List<String> getListOfDrivers() {
        List<String> driverNames = new ArrayList<>();

        File[] drivers  = new File(System.getProperty("user.dir")).listRoots();

        for(File driver : drivers) {
            driverNames.add(driver.getPath());
        }

        return driverNames;
    }
}
