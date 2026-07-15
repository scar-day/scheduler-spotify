package dev.scarday.telegramspotify.download.impl;

import dev.scarday.telegramspotify.download.DownloadRequest;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class AlbumTrackDownloadRequest extends DownloadRequest {
    String albumId;

    public String albumId() {
        return albumId;
    }

    public static AlbumTrackDownloadRequest of(SpotifyTrack track) {
        return AlbumTrackDownloadRequest.builder()
                .query("%s - %s".formatted(track.artists(), track.getSongName()))
                .albumId(track.getAlbumId())
                .build();
    }
}
