package com.rems.common.util;

import java.util.UUID;

public class FileUploadUtil {

    public static String generateFileName(String original) {
        String ext = original.substring(original.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }
}