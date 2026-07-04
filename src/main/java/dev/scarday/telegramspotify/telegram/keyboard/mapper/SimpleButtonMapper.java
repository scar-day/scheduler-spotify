package dev.scarday.telegramspotify.telegram.keyboard.mapper;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.AbstractButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.LinkButton;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Objects;

@Component
public class SimpleButtonMapper implements ButtonMapper{
    @Override
    public InlineKeyboardButton toButton(AbstractButton abstractButton) {
        return switch (abstractButton) {
            case CallbackButton callbackButton -> buildTextButton(callbackButton);
            case LinkButton linkButton -> buildLinkButton(linkButton);
            default -> throw new IllegalStateException("Unexpected value: " + abstractButton);
        };
    }

    private InlineKeyboardButton buildTextButton(@NonNull CallbackButton textButton) {
        return InlineKeyboardButton.builder()
                .text(textButton.getLabel())
                .callbackData(textButton.getAction())
                .style(style(textButton))
                .build();
    }

    private InlineKeyboardButton buildLinkButton(@NonNull LinkButton linkButton) {
        return InlineKeyboardButton.builder()
                .text(linkButton.getLabel())
                .url(linkButton.getUrl())
                .style(style(linkButton))
                .build();
    }

    private String style(AbstractButton abstractButton) {
        return Objects.requireNonNullElse(abstractButton.getStyle(), "");
    }
}
