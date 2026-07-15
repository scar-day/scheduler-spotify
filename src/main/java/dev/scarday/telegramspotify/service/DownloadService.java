package dev.scarday.telegramspotify.service;

import dev.scarday.telegramspotify.download.DownloadRequest;
import dev.scarday.telegramspotify.download.DownloadResult;
import dev.scarday.telegramspotify.download.impl.AlbumTrackDownloadRequest;
import dev.scarday.telegramspotify.download.impl.TrackDownloadRequest;
import dev.scarday.telegramspotify.download.platform.Platform;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DownloadService {
    Platform<DownloadRequest, DownloadResult> downloadPlatform;

    public DownloadResult downloadTrack(SpotifyTrack track) {
        return downloadPlatform.execute(TrackDownloadRequest.of(track));
    }

    public DownloadResult downloadAlbumTrack(SpotifyTrack track) {
        return downloadPlatform.execute(AlbumTrackDownloadRequest.of(track));
    }
}
