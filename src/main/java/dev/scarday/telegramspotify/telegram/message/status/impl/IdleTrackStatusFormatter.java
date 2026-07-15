package dev.scarday.telegramspotify.telegram.message.status.impl;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.telegram.message.status.TrackStatusFormatter;
import dev.scarday.telegramspotify.utility.BarUtility;
import jakarta.annotation.Nullable;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class IdleTrackStatusFormatter implements TrackStatusFormatter {
    private static final String[] IDLE_TITLES = {
            "Тишина...",
            "Пусто...",
            "Никто ничего не слушает..."
    };

    @Override
    public boolean supports(@Nullable SpotifyTrack track) {
        return track == null || (!track.isPlayed() && !track.isPaused());
    }

    @Override
    public String format(@Nullable SpotifyTrack track) {
        long tick = System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(15);
        val title = IDLE_TITLES[(int) ((tick / 4) % IDLE_TITLES.length)];

        return String.format(
                """
                        😴 *%s*

                        `%s`""",
                title, BarUtility.sleepWave(tick)
        );
    }
}
