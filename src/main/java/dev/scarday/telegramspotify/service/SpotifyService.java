package dev.scarday.telegramspotify.service;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpotifyService {
    SpotifyApi spotifyApi;

    @SneakyThrows
    public SpotifyTrack getCurrentTrack() {
        val accessToken = spotifyApi.authorizationCodeRefresh().build().execute().getAccessToken();
        spotifyApi.setAccessToken(accessToken);

        val currentlyPlaying = spotifyApi.getUsersCurrentlyPlayingTrack().build().execute();

        if (currentlyPlaying == null) {
            return SpotifyTrack.EMPTY;
        }

        if (currentlyPlaying.getItem() instanceof Track track) {
            val artists = Arrays.stream(track.getArtists())
                    .map(ArtistSimplified::getName)
                    .toList();

            val trackName = track.getName();
            val isPlaying = currentlyPlaying.getIs_playing();

            val images = track.getAlbum().getImages();
            val coverUrl = (images != null && images.length > 0) ? images[0].getUrl() : null;

            if (!isPlaying) {
                return SpotifyTrack.builder()
                        .id(track.getId())
                        .played(false)
                        .paused(true)
                        .songName(trackName)
                        .artists(artists)
                        .coverUrl(coverUrl)
                        .progressMs(currentlyPlaying.getProgress_ms())
                        .durationMs(currentlyPlaying.getItem().getDurationMs())
                        .build();
            }

            return SpotifyTrack.builder()
                    .id(track.getId())
                    .played(true)
                    .paused(false)
                    .songName(trackName)
                    .artists(artists)
                    .coverUrl(coverUrl)
                    .progressMs(currentlyPlaying.getProgress_ms())
                    .durationMs(currentlyPlaying.getItem().getDurationMs())
                    .build();
        }

        return SpotifyTrack.MAYBE_PODCAST;
    }
}
