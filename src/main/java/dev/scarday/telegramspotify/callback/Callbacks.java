package dev.scarday.telegramspotify.callback;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Callbacks {
    public static final String DOWNLOAD = "download";

    public static String getAction(String callbackData) {
        int index = callbackData.indexOf(':');

        if (index == -1) {
            return callbackData;
        }

        return callbackData.substring(0, index);
    }

    public static String getArgument(String callbackData) {
        int index = callbackData.indexOf(':');

        if (index == -1) {
            throw new IllegalArgumentException("Invalid callback data: " + callbackData);
        }

        return callbackData.substring(index + 1);
    }
}
