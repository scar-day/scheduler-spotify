package dev.scarday.telegramspotify.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AlbumDownloadTracker {
    private final ConcurrentHashMap<String, Long> activeDownloads = new ConcurrentHashMap<>();

    public void startDownload(String albumId) {
        activeDownloads.put(albumId, System.currentTimeMillis());
        log.debug("Started download for album {}", albumId);
    }

    public void finishDownload(String albumId) {
        activeDownloads.remove(albumId);
        log.debug("Finished download for album {}", albumId);
    }

    public boolean isDownloading(String albumId) {
        return activeDownloads.containsKey(albumId);
    }
}
