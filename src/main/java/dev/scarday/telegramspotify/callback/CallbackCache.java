package dev.scarday.telegramspotify.callback;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CallbackCache {

    private final Cache<String, SpotifyTrack> callbacks = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(15))
            .build();

    public void register(SpotifyTrack track) {
        callbacks.invalidateAll();
        callbacks.put(track.getId(), track);
    }

    public SpotifyTrack get(String id) {
        return callbacks.getIfPresent(id);
    }
}