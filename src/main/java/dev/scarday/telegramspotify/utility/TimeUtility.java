package dev.scarday.telegramspotify.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtility {
    public static String formatTime(long ms) {
        long minutes = (ms / 1000) / 60;
        long seconds = (ms / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
