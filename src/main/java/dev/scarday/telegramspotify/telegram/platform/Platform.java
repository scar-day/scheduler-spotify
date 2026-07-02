package dev.scarday.telegramspotify.telegram.platform;

import dev.scarday.telegramspotify.telegram.message.MessageMapper;

public interface Platform {
    void execute(MessageMapper messageMapper);

    void answerQuery(String id, String message);
}
