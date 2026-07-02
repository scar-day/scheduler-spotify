package dev.scarday.telegramspotify.telegram.keyboard.mapper;

import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SimpleKeyboardMapper implements KeyboardMapper {
    ButtonMapper buttonMapper;

    @Override
    public InlineKeyboardMarkup toKeyboard(Keyboard keyboard) {
        val rows = keyboard.getRows()
                .stream()
                .map(this::resolveRow)
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    private InlineKeyboardRow resolveRow(Keyboard.Row row) {
        return new InlineKeyboardRow(
                row.buttons().stream()
                        .map(buttonMapper::toButton)
                        .toList()
        );
    }
}
