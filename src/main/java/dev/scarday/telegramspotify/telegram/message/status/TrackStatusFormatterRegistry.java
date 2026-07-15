package dev.scarday.telegramspotify.telegram.message.status;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackStatusFormatterRegistry {
    List<TrackStatusFormatter> formatters;

    public String format(@Nullable SpotifyTrack track) {
        return formatters.stream()
                .filter(formatter -> formatter.supports(track))
                .findFirst()
                .map(formatter -> formatter.format(track))
                .orElseThrow(() -> new IllegalStateException("No formatter supports track state"));
    }
}
