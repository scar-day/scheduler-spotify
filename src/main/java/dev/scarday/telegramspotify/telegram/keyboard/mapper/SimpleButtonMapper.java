package dev.scarday.telegramspotify.telegram.keyboard.mapper;

import dev.scarday.telegramspotify.telegram.message.keyboard.Button;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class SimpleButtonMapper implements ButtonMapper{
    @Override
    public InlineKeyboardButton toButton(Button button) {
        val style = button.getStyle();

        return InlineKeyboardButton.builder()
                .text(button.getLabel())
                .callbackData("%s:%s".formatted(button.getAction(), button.getData()))
                .style(style != null ? style : "")
                .build();
    }
}
