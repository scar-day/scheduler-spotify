package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.LinkButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
import lombok.NonNull;

public interface ButtonFactory {
    CallbackButton createMP3FileButton(@NonNull String musicId);
    CallbackButton createAlbumButton(@NonNull String albumId);
    LinkButton createSourceCodeButton();

    CallbackButton createDownloadChoiceButton(@NonNull String trackId);
}
