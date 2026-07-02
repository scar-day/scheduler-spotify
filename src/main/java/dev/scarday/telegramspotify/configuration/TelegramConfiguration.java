package dev.scarday.telegramspotify.configuration;

import lombok.Getter;
import lombok.Setter;
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
public class TelegramConfiguration {

    private Bot bot = new Bot();
    private Message message = new Message();
    private List<Long> ids = new ArrayList<>();

    @Getter
    @Setter
    public static class Bot {
        private String token;
    }

    @Getter
    @Setter
    public static class Message {
        private String chatId;
        private int id;
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(bot.getToken());
    }
}