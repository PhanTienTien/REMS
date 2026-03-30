package com.rems.common.util;

import jakarta.servlet.ServletContext;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {

    private static final String LEGACY_UPLOAD_DIR = "D:/Java/KeySoft/KeyBDS/Image";

    private FileUploadUtil() {
    }

    public static String generateFileName(String original) {
        String ext = original.substring(original.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    public static File getUploadDirectory(ServletContext servletContext) {
        String realPath = servletContext.getRealPath("/uploads");
        File uploadDir;

        if (realPath != null && !realPath.isBlank()) {
            uploadDir = new File(realPath);
        } else {
            Path fallback = Paths.get(System.getProperty("user.dir"), "uploads");
            uploadDir = fallback.toFile();
        }

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        return uploadDir;
    }

    public static File getLegacyUploadDirectory() {
        File dir = new File(LEGACY_UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
