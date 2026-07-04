package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.telegram.keyboard.constants.ButtonStyle;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.LinkButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
import org.jspecify.annotations.NonNull;
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
}
