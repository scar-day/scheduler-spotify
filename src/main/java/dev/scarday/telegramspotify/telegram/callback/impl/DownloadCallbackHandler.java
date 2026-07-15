package dev.scarday.telegramspotify.telegram.callback.impl;

import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.service.DownloadService;
import dev.scarday.telegramspotify.telegram.callback.CallbackHandler;
import dev.scarday.telegramspotify.telegram.message.impl.SendAudioMethod;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DownloadCallbackHandler implements CallbackHandler {
    CallbackCache callbackCache;
    TelegramPlatform platform;
    DownloadService downloadService;

    private final static String TEMPLATE_MESSAGE =
                    """
                    ✅ %s — %s.
                    — Трек успешно скачан.
                    """;

    @Override
    public String action() {
        return Callbacks.MP3;
    }

    @Override
    public void handle(CallbackQuery query) {
        val trackId = Callbacks.getArgument(query.getData());
        val track = callbackCache.get(trackId);

        val queryId = query.getId();

        if (track == null) {
            platform.answerQuery(
                    queryId,
                    "⚠️ Этот трек уже недоступен."
            );
            return;
        }

        platform.answerQuery(
                queryId,
                "⏳ Начинаю скачивание..."
        );

        try {
            val resultDownload = downloadService.downloadTrack(track);

            platform.execute(
                    SendAudioMethod.builder()
                            .chatId(String.valueOf(query.getFrom().getId()))
                            .text(TEMPLATE_MESSAGE.formatted(track.artists(), track.getSongName()))
                            .audio(resultDownload.audio())
                            .thumbnail(resultDownload.thumbnail())
                            .performer(track.artists())
                            .title(track.getSongName())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to download track {}", track.getSongName(), e);
        }
    }
}

