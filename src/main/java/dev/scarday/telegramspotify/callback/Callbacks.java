package dev.scarday.telegramspotify.callback;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Callbacks {
    public static final String REPOST = "repost";

    public static String repost(String trackId) {
        return REPOST + ":" + trackId;
    }

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
