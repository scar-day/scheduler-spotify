package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SendMessageMethod extends MessageMapper {
    @Override
    public PartialBotApiMethod<?> toApiMethod(KeyboardMapper keyboardMapper) {
        val message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.MARKDOWN);

        if (keyboard != null) {
            message.replyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return message.build();
    }
}
