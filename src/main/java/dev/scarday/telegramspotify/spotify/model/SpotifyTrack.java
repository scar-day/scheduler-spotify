package dev.scarday.telegramspotify.spotify.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static dev.scarday.telegramspotify.utility.TimeUtility.formatTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class SpotifyTrack {
    public static final SpotifyTrack EMPTY = SpotifyTrack.builder()
            .songName("Ничего не играет.")
            .build();
    public static final SpotifyTrack MAYBE_PODCAST = SpotifyTrack.builder()
            .songName("Неизвестно что играет, возможно играет подкаст.")
            .build();

    @Builder.Default
    boolean played = false;

    @Builder.Default
    boolean paused = false;

    List<String> artists;
    String songName;

    String coverUrl;

    long progressMs;
    long durationMs;

    public String getProgressFormatted() {
        return formatTime(progressMs);
    }

    public String getDurationFormatted() {
        return formatTime(durationMs);
    }

    public String getArtistsList() {
        if (artists == null) return null;
        return String.join(", ", artists);
    }
}
