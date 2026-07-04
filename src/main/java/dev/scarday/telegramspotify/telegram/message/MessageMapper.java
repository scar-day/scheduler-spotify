package dev.scarday.telegramspotify.telegram.message;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class MessageMapper {
    String chatId;
    String text;
    @Nullable Keyboard keyboard;

    public abstract PartialBotApiMethod<?> toApiMethod(KeyboardMapper keyboardMapper);
}
