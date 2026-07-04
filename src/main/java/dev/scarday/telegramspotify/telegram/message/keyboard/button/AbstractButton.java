package dev.scarday.telegramspotify.telegram.message.keyboard.button;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractButton {
    @NonNull String label;
    @Nullable String style;
}
