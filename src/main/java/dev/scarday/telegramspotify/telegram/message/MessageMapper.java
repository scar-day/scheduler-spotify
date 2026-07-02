package dev.scarday.telegramspotify.telegram.message;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@SuperBuilder
@RequiredArgsConstructor
public abstract class MessageMapper {
    public abstract PartialBotApiMethod<?> toApiMethod(KeyboardMapper keyboardMapper);
}
