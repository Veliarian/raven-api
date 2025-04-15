package com.raven.api.files.utils;

import com.raven.api.files.enums.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeUtils {
    private static final Map<String, String> mimeToExtensionMap = new HashMap<>();

    static {
        // Images
        mimeToExtensionMap.put("image/jpeg", ".jpg");
        mimeToExtensionMap.put("image/png", ".png");
        mimeToExtensionMap.put("image/gif", ".gif");
        mimeToExtensionMap.put("image/webp", ".webp");
        mimeToExtensionMap.put("image/svg+xml", ".svg");
        mimeToExtensionMap.put("image/bmp", ".bmp");
        mimeToExtensionMap.put("image/tiff", ".tiff");

        // Videos
        mimeToExtensionMap.put("video/mp4", ".mp4");
        mimeToExtensionMap.put("video/x-msvideo", ".avi");
        mimeToExtensionMap.put("video/mpeg", ".mpeg");
        mimeToExtensionMap.put("video/quicktime", ".mov");
        mimeToExtensionMap.put("video/x-matroska", ".mkv");
        mimeToExtensionMap.put("video/webm", ".webm");

        // Audio
        mimeToExtensionMap.put("audio/mpeg", ".mp3");
        mimeToExtensionMap.put("audio/wav", ".wav");
        mimeToExtensionMap.put("audio/ogg", ".ogg");
        mimeToExtensionMap.put("audio/webm", ".weba");

        // Office docs
        mimeToExtensionMap.put("application/pdf", ".pdf");
        mimeToExtensionMap.put("application/msword", ".doc");
        mimeToExtensionMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        mimeToExtensionMap.put("application/vnd.ms-excel", ".xls");
        mimeToExtensionMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        mimeToExtensionMap.put("application/vnd.ms-powerpoint", ".ppt");
        mimeToExtensionMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");

        // Text
        mimeToExtensionMap.put("text/plain", ".txt");
        mimeToExtensionMap.put("text/csv", ".csv");
        mimeToExtensionMap.put("text/html", ".html");
    }

    public static String getExtension(String contentType) {
        return mimeToExtensionMap.getOrDefault(contentType, "");
    }

    public static MediaType getMediaType(String contentType) {
        if (contentType == null) return MediaType.OTHER;

        if (contentType.startsWith("image/")) {
            return MediaType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MediaType.VIDEO;
        } else if (contentType.startsWith("audio/")) {
            return MediaType.AUDIO;
        } else if (contentType.equals("application/pdf")
                || contentType.equals("application/msword")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || contentType.equals("application/vnd.ms-excel")
                || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || contentType.equals("text/plain")
                || contentType.equals("text/csv")) {
            return MediaType.DOCUMENT;
        } else if (contentType.equals("application/vnd.ms-powerpoint")
                || contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
            return MediaType.PRESENTATION;
        }

        return MediaType.OTHER;
    }
}
