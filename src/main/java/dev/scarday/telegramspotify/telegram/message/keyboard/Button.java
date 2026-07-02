package dev.scarday.telegramspotify.telegram.message.keyboard;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Button {
    @NonNull String label;
    @Nullable String style;
    @NonNull String data;
    @NonNull String action;
}
