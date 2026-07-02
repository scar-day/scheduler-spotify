package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.telegram.message.keyboard.Button;
import lombok.NonNull;

public interface ButtonFactory {
    Button createRepostButton(@NonNull String data);
}
