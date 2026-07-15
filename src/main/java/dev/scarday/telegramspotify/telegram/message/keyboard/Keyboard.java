package dev.scarday.telegramspotify.telegram.message.keyboard;

import dev.scarday.telegramspotify.telegram.message.keyboard.button.AbstractButton;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Keyboard {
    List<Row> rows;

    public static KeyboardBuilder builder() {
        return new KeyboardBuilder();
    }


    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class KeyboardBuilder {
        List<Row> rows = new ArrayList<>();

        public KeyboardBuilder row(AbstractButton... buttons) {
            rows.add(new Row(List.of(buttons)));
            return this;
        }

        public KeyboardBuilder rowIf(boolean condition, AbstractButton... buttons) {
            if (condition) {
                row(buttons);
            }
            return this;
        }

        public Keyboard build() {
            return new Keyboard(rows);
        }
    }

    public record Row(List<AbstractButton> buttons) {}
}
