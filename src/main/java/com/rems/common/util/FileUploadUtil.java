package com.rems.common.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FileUploadUtil {

    private static final String LEGACY_UPLOAD_DIR = System.getenv().getOrDefault("REMS_UPLOAD_DIR",
            System.getProperty("user.home") + "/rems-uploads");

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    ));

    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    ));

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private FileUploadUtil() {
    }

    public static String generateFileName(String original) {
        int dotIndex = original.lastIndexOf(".");
        if (dotIndex <= 0) {
            throw new IllegalArgumentException("Invalid file name: no extension found");
        }
        String ext = original.substring(dotIndex);
        return UUID.randomUUID() + ext;
    }

    public static boolean isValidImageFile(Part part) {
        if (part == null || part.getSize() <= 0 || part.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        String fileName = getSafeFileName(part.getSubmittedFileName());
        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        String ext = getFileExtension(fileName).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return false;
        }

        String contentType = part.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            return false;
        }

        return true;
    }

    public static String getSafeFileName(String submittedFileName) {
        if (submittedFileName == null || submittedFileName.isBlank()) {
            return null;
        }

        String fileName = Paths.get(submittedFileName).getFileName().toString();

        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return null;
        }

        return fileName;
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
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
