package dev.scarday.telegramspotify.telegram.message.keyboard.factory;

import dev.scarday.telegramspotify.cache.Callbacks;
import dev.scarday.telegramspotify.telegram.keyboard.constants.ButtonStyle;
import dev.scarday.telegramspotify.telegram.message.keyboard.Button;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SimpleButtonFactory implements ButtonFactory {
    @Override
    public Button createRepostButton(@NonNull String data) {
        return Button.builder()
                .style(ButtonStyle.GREEN)
                .label("Сделать репост данного трека.")
                .data(data)
                .action(Callbacks.REPOST)
                .build();
    }
}
