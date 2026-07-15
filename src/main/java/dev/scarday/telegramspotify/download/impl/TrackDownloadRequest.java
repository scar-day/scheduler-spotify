package dev.scarday.telegramspotify.download.impl;

import dev.scarday.telegramspotify.download.DownloadRequest;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TrackDownloadRequest extends DownloadRequest {
    public static TrackDownloadRequest of(SpotifyTrack track) {
        return TrackDownloadRequest.builder()
                .query("%s - %s".formatted(track.artists(), track.getSongName()))
                .build();
    }

    public static TrackDownloadRequest of(String query) {
        return TrackDownloadRequest.builder()
                .query(query)
                .build();
    }
}
