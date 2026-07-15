package dev.scarday.telegramspotify.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileNameUtility {
    public static String audioFileName(String artist, String title) {
        String artistPart = sanitize(artist);
        String titlePart = sanitize(title);

        String name = (artistPart.isBlank() ? "" : artistPart + " - ") + titlePart;
        if (name.isBlank()) {
            name = "track";
        }

        return name + ".mp3";
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "";
        }

        return value.replaceAll("[\\\\/:*?\"<>|]", "").trim();
    }
}
