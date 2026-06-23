package dev.scarday.telegramspotify.telegram.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class TelegramConfiguration {
    @Value("${telegram.bot.token}")
    String botToken;

    @Value("${telegram.message.chat-id}")
    String chatId;
    @Value("${telegram.message.id}")
    int targetMessageId;
}
