package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.LinkButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
import lombok.NonNull;

public interface ButtonFactory {
    CallbackButton createDownloadButton(@NonNull String musicId);
    LinkButton createSourceCodeButton();
}
