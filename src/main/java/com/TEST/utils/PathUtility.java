package com.TEST.utils;

import java.io.File;

public class PathUtility {

    public static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + path);
        }
    }
    public static String getRunDirectory() {
        return RunManager.getRunDirectory();
    }
}
