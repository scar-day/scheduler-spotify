package dev.scarday.telegramspotify.telegram.message.status.impl;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.telegram.message.status.TrackStatusFormatter;
import dev.scarday.telegramspotify.utility.BarUtility;
import jakarta.annotation.Nullable;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class PlayingTrackStatusFormatter implements TrackStatusFormatter {
    @Override
    public boolean supports(@Nullable SpotifyTrack track) {
        return track != null && track.isPlayed();
    }

    @Override
    public String format(@Nullable SpotifyTrack track) {
        val updatedAt = "*" + FORMATTER.format(ZonedDateTime.now(MOSCOW_ZONE)) + "* (GMT+3)";

        return String.format(
                """
                        🎧 *Сейчас играет*
                        `%s — %s`

                        %s
                        ⏱ `%s / %s`

                        🕒 Обновлено: %s
                        [‎](%s)""",
                track.artists(),
                track.getSongName(),
                BarUtility.progressBar(track.getProgressMs(), track.getDurationMs()),
                track.getProgressFormatted(),
                track.getDurationFormatted(),
                updatedAt, track.getCoverUrl()
        );
    }
}
