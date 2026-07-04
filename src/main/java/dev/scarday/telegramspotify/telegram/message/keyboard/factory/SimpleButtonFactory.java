package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.telegram.keyboard.constants.ButtonStyle;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.LinkButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SimpleButtonFactory implements ButtonFactory {
    @Override
    public CallbackButton createDownloadButton(@NonNull String musicId) {
        return CallbackButton.builder()
                .style(ButtonStyle.GREEN)
                .label("Скачать данный трек")
                .data(musicId)
                .action(Callbacks.DOWNLOAD)
                .build();
    }

    @Override
    public LinkButton createSourceCodeButton() {
        return LinkButton.builder()
                .url("https://github.com/scar-day/scheduler-spotify")
                .label("Исходный код проекта")
                .style(ButtonStyle.GREEN)
                .build();
    }

    /* TODO */
    public CallbackButton createMP3FileButton(@NonNull String musicId) {
        return CallbackButton.builder()
                .style(ButtonStyle.GREEN)
                .label("MP3 Файл")
                .data(musicId)
                .action(Callbacks.MP3)
                .build();
    }

    public CallbackButton createAlbumButton(@NonNull String albumId) {
        return CallbackButton.builder()
                .style(ButtonStyle.BLUE)
                .label("Альбом")
                .data(albumId)
                .action(Callbacks.ALBUM)
                .build();
    }
    /* TODO */
}
