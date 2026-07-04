package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EditMessageTextMethod extends MessageMapper {
    int messageId;

    @Override
    public EditMessageText toApiMethod(KeyboardMapper keyboardMapper) {
        val edit = EditMessageText.builder()
                .chatId(chatId)
                .text(text)
                .messageId(messageId)
                .parseMode(ParseMode.MARKDOWN)
                .build();

        if (keyboard != null) {
            edit.setReplyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return edit;
    }
}
