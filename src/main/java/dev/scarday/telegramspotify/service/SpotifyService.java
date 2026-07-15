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
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpotifyService {
    SpotifyApi spotifyApi;

    @SneakyThrows
    public SpotifyTrack getCurrentTrack() {
        refreshAccessToken();

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

            val album = track.getAlbum();
            val images = album == null ? null : album.getImages();
            val coverUrl = (images != null && images.length > 0) ? images[0].getUrl() : null;
            val albumId = album == null ? null : album.getId();
            val albumName = album == null ? null : album.getName();
            val albumType = album == null || album.getAlbumType() == null
                    ? null
                    : album.getAlbumType().toString();

            if (!isPlaying) {
                return SpotifyTrack.builder()
                        .id(track.getId())
                        .albumId(albumId)
                        .albumName(albumName)
                        .albumType(albumType)
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
                    .albumId(albumId)
                    .albumName(albumName)
                    .albumType(albumType)
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

    @SneakyThrows
    public List<SpotifyTrack> getAlbumTracks(String albumId) {
        if (albumId == null || albumId.isBlank()) {
            return List.of();
        }

        refreshAccessToken();

        val album = spotifyApi.getAlbum(albumId).build().execute();
        val albumName = album.getName();
        val albumType = album.getAlbumType() == null ? null : album.getAlbumType().toString();
        val images = album.getImages();
        val coverUrl = images != null && images.length > 0 ? images[0].getUrl() : null;
        val tracks = new ArrayList<SpotifyTrack>();

        int offset = 0;
        Paging<TrackSimplified> page;
        do {
            page = spotifyApi.getAlbumsTracks(albumId)
                    .limit(50)
                    .offset(offset)
                    .build()
                    .execute();

            if (page == null || page.getItems() == null || page.getItems().length == 0) {
                break;
            }

            for (val track : page.getItems()) {
                val artists = track.getArtists() == null
                        ? List.<String>of()
                        : Arrays.stream(track.getArtists())
                        .map(ArtistSimplified::getName)
                        .toList();

                tracks.add(SpotifyTrack.builder()
                        .id(track.getId())
                        .albumId(albumId)
                        .albumName(albumName)
                        .albumType(albumType)
                        .artists(artists)
                        .songName(track.getName())
                        .coverUrl(coverUrl)
                        .build());
            }

            offset += page.getItems().length;
        } while (page.getNext() != null);

        return tracks;
    }

    private void refreshAccessToken() throws Exception {
        val accessToken = spotifyApi.authorizationCodeRefresh().build().execute().getAccessToken();
        spotifyApi.setAccessToken(accessToken);
    }
}
