package dev.scarday.telegramspotify.telegram.bot;

import dev.scarday.telegramspotify.telegram.configuration.TelegramConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    TelegramConfiguration telegramConfiguration;
    TelegramClient client;

    @NonFinal
    TelegramBotsLongPollingApplication longPollingApplication;

    public TelegramBot(TelegramConfiguration telegramConfiguration) {
        this.telegramConfiguration = telegramConfiguration;
        this.client = new OkHttpTelegramClient(telegramConfiguration.getBotToken());
    }

    @PostConstruct
    public void start() throws TelegramApiException {
        longPollingApplication = new TelegramBotsLongPollingApplication();
        longPollingApplication.registerBot(telegramConfiguration.getBotToken(), this);
        log.info("🚀 Telegram LongPolling бот успешно запущен!");
    }

    @PreDestroy
    @SneakyThrows
    public void stop() {
        if (longPollingApplication != null) {
            longPollingApplication.close();
        }
    }

    public void updateMessage(String text) {
        try {
            val edit = EditMessageText.builder()
                    .chatId(telegramConfiguration.getChatId())
                    .messageId(telegramConfiguration.getTargetMessageId())
                    .text(text)
                    .parseMode("Markdown")
                    .build();

            client.execute(edit);
        } catch (TelegramApiException e) {
            if (!e.getMessage().contains("message is not modified")) {
                log.error("Ошибка обновления сообщения: {}", e.getMessage());
            }
        }
    }

    @Override
    public void consume(Update update) {
    }
}
