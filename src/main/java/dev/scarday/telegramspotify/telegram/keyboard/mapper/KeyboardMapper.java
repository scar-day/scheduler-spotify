package dev.scarday.telegramspotify.telegram.keyboard.mapper;

import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyboardMapper {
    InlineKeyboardMarkup toKeyboard(Keyboard keyboard);
}
