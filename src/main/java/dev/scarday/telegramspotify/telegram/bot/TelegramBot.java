package dev.scarday.telegramspotify.telegram.bot;

import dev.scarday.telegramspotify.configuration.TelegramConfiguration;
import dev.scarday.telegramspotify.telegram.callback.CallbackDispatcher;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    TelegramConfiguration telegramConfiguration;
    TelegramPlatform telegramPlatform;
    CallbackDispatcher callbackDispatcher;

    @NonFinal
    TelegramBotsLongPollingApplication longPollingApplication;

    @PostConstruct
    public void start() throws TelegramApiException {
        longPollingApplication = new TelegramBotsLongPollingApplication();
        longPollingApplication.registerBot(telegramConfiguration.getBot().getToken(), this);
    }

    @PreDestroy
    @SneakyThrows
    public void stop() {
        if (longPollingApplication != null) {
            longPollingApplication.close();
        }
    }

    @Override
    public void consume(Update update) {
        val query = update.getCallbackQuery();

        if (query == null) return;

        if (!telegramConfiguration.getIds().contains(query.getFrom().getId())) {
            telegramPlatform.answerQuery(
                    query.getId(),
                    "⚠️ Это не для вас."
            );
            return;
        }

        callbackDispatcher.dispatch(query);
    }
}
