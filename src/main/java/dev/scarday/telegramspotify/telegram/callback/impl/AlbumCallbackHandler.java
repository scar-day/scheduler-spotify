package dev.scarday.telegramspotify.telegram.callback.impl;

import dev.scarday.telegramspotify.callback.AlbumDownloadTracker;
import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.download.DownloadResult;
import dev.scarday.telegramspotify.service.DownloadService;
import dev.scarday.telegramspotify.service.SpotifyService;
import dev.scarday.telegramspotify.telegram.callback.CallbackHandler;
import dev.scarday.telegramspotify.telegram.message.impl.SendAudioMethod;
import dev.scarday.telegramspotify.telegram.message.impl.SendMediaGroupMethod;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumCallbackHandler implements CallbackHandler {
    private static final int TELEGRAM_MEDIA_GROUP_SIZE = 10;
    private static final long TELEGRAM_MEDIA_GROUP_MAX_BYTES = 45L * 1024 * 1024;

    CallbackCache callbackCache;
    SpotifyService spotifyService;
    DownloadService downloadService;
    TelegramPlatform platform;
    AlbumDownloadTracker albumDownloadTracker;
    ExecutorService albumDownloadExecutor;

    @Override
    public String action() {
        return Callbacks.ALBUM;
    }

    @Override
    public void handle(CallbackQuery query) {
        val data = Callbacks.getArgument(query.getData());
        val currentTrack = callbackCache.get(data);
        val albumId = currentTrack != null ? currentTrack.getAlbumId() : null;
        val queryId = query.getId();

        if (albumId == null) {
            platform.answerQuery(queryId, "⚠️ Альбом недоступен.");
            return;
        }

        platform.answerQuery(queryId, "Начинаю скачивание альбома...");
        albumDownloadTracker.startDownload(albumId);

        try {
            val tracks = spotifyService.getAlbumTracks(albumId);
            if (tracks.isEmpty()) {
                log.warn("Album {} has no available tracks", albumId);
                return;
            }

            val albumName = currentTrack == null || currentTrack.getAlbumName() == null
                    ? "Альбом"
                    : currentTrack.getAlbumName();

            val futures = new ArrayList<Future<DownloadResult>>(tracks.size());
            for (val track : tracks) {
                futures.add(albumDownloadExecutor.submit(() -> {
                    log.debug("[{}] Скачиваю трек: {} — {}",
                            Thread.currentThread().getName(), track.artists(), track.getSongName());
                    return downloadService.downloadAlbumTrack(track);
                }));
            }

            val audioFiles = new ArrayList<SendMediaGroupMethod.AudioFile>();
            long batchBytes = 0L;

            for (int i = 0; i < tracks.size(); i++) {
                val track = tracks.get(i);
                try {
                    val result = futures.get(i).get();
                    if (result.audio() == null || result.audio().length == 0) {
                        log.warn("yt-dlp returned an empty audio for {}", track.getSongName());
                        continue;
                    }

                    long trackBytes = result.audio().length
                            + (result.thumbnail() != null ? result.thumbnail().length : 0);

                    if (!audioFiles.isEmpty()
                            && (audioFiles.size() == TELEGRAM_MEDIA_GROUP_SIZE
                                || batchBytes + trackBytes > TELEGRAM_MEDIA_GROUP_MAX_BYTES)) {
                        sendBatch(query, albumName, audioFiles);
                        audioFiles.clear();
                        batchBytes = 0L;
                    }

                    audioFiles.add(new SendMediaGroupMethod.AudioFile(
                            track.artists(), track.getSongName(), result.audio(), result.thumbnail()));
                    batchBytes += trackBytes;
                } catch (Exception e) {
                    log.error("Failed to download album track {}", track.getSongName(), e);
                }
            }

            if (!audioFiles.isEmpty()) {
                sendBatch(query, albumName, audioFiles);
            }
        } catch (Exception e) {
            log.error("Failed to download album {}", albumId, e);
        } finally {
            albumDownloadTracker.finishDownload(albumId);
        }
    }

    private void sendBatch(
            CallbackQuery query,
            String albumName,
            List<SendMediaGroupMethod.AudioFile> audioFiles
    ) {
        if (audioFiles.size() == 1) {
            sendSingle(query, albumName, audioFiles.getFirst());
            return;
        }

        platform.execute(
                SendMediaGroupMethod.builder()
                        .chatId(String.valueOf(query.getFrom().getId()))
                        .text("Альбом «%s» скачан.".formatted(albumName))
                        .audios(List.copyOf(audioFiles))
                        .build()
        );
    }

    private void sendSingle(CallbackQuery query, String albumName, SendMediaGroupMethod.AudioFile audioFile) {
        platform.execute(
                SendAudioMethod.builder()
                        .chatId(String.valueOf(query.getFrom().getId()))
                        .text("Альбом «%s» скачан.".formatted(albumName))
                        .audio(audioFile.audio())
                        .thumbnail(audioFile.thumbnail())
                        .performer(audioFile.artist())
                        .title(audioFile.title())
                        .build()
        );
    }
}
