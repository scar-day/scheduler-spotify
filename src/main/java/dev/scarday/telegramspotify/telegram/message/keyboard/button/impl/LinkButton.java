package dev.scarday.telegramspotify.telegram.message.keyboard.button.impl;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.AbstractButton;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class LinkButton extends AbstractButton {
    @NonNull String url;
}
