package dev.scarday.telegramspotify.telegram.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {
    String action();

    void handle(CallbackQuery query);
}
