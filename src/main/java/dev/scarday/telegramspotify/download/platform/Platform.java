package dev.scarday.telegramspotify.download.platform;

public interface Platform<REQUEST, RESULT> {
    RESULT execute(REQUEST request);
}
