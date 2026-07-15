package dev.scarday.telegramspotify.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "telegram")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramConfiguration {

    Bot bot = new Bot();
    Message message = new Message();
    List<Long> ids = new ArrayList<>();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PUBLIC)
    public static class Bot {
        String token;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PUBLIC)
    public static class Message {
        String chatId;
        int id;
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(bot.getToken());
    }
}