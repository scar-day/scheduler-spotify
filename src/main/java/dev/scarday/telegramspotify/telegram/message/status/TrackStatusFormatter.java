package dev.scarday.telegramspotify.telegram.message.status;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import jakarta.annotation.Nullable;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface TrackStatusFormatter {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    boolean supports(@Nullable SpotifyTrack track);

    String format(@Nullable SpotifyTrack track);
}
