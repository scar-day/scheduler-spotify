package dev.scarday.telegramspotify.telegram.message.keyboard.button.impl;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.AbstractButton;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackButton extends AbstractButton {
    @NonNull String action;
    @NonNull String data;

    public String getAction() {
        return "%s:%s".formatted(action, data);
    }
}
