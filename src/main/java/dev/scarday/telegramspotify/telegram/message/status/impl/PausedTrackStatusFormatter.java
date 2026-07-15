package dev.scarday.telegramspotify.telegram.message.status.impl;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.telegram.message.status.TrackStatusFormatter;
import jakarta.annotation.Nullable;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class PausedTrackStatusFormatter implements TrackStatusFormatter {
    @Override
    public boolean supports(@Nullable SpotifyTrack track) {
        return track != null && track.isPaused();
    }

    @Override
    public String format(@Nullable SpotifyTrack track) {
        val updatedAt = "*" + FORMATTER.format(ZonedDateTime.now(MOSCOW_ZONE)) + "* (GMT+3)";

        return String.format(
                """
                        ⏸ *На паузе*
                        `%s — %s`

                        ⏱ Остановлено: `%s / %s`

                        🕒 Обновлено: %s
                        [‎](%s)""",
                track.artists(),
                track.getSongName(),
                track.getProgressFormatted(),
                track.getDurationFormatted(),
                updatedAt, track.getCoverUrl()
        );
    }
}
