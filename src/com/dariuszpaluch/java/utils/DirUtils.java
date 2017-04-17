package com.dariuszpaluch.java.utils;

import com.dariuszpaluch.java.utils.visitor.DeleteDirVisitor;
import com.dariuszpaluch.java.utils.visitor.GetSizeDirVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class DirUtils {

    /**
     * If the path exists, completely removes given file tree starting at and including the given path.
     *
     * @param path
     * @throws IOException
     */
    public static void deleteIfExists(Path path) throws IOException {
        if (Files.exists(path)) {
            validate(path);
            Files.walkFileTree(path, new DeleteDirVisitor());
        }
    }

    public static long getTotalSize(Path path) throws IOException {
//        if (Files.exists(path)) {
//            validate(path);
//            GetSizeDirVisitor visitor = new GetSizeDirVisitor();
//            Files.walkFileTree(path, visitor);
//            return visitor.getTotalSize();
//        }

        return 0;
    }


    private static void validate(Path... paths) {
        for (Path path : paths) {
            Objects.requireNonNull(path);
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException(String.format("%s is not a directory", path.toString()));
            }
        }
    }

}
