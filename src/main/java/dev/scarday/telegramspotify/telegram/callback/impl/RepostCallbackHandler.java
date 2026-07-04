package dev.scarday.telegramspotify.telegram.callback.impl;

import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.service.YtDlpService;
import dev.scarday.telegramspotify.telegram.callback.CallbackHandler;
import dev.scarday.telegramspotify.telegram.message.impl.SendAudioMethod;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RepostCallbackHandler implements CallbackHandler {
    CallbackCache callbackCache;
    TelegramPlatform platform;
    YtDlpService ytDlpService;

    private final static String TEMPLATE_MESSAGE =
                    """
                    ✅ %s — %s.
                    — Трек успешно скачан.
                    """;

    @Override
    public String action() {
        return Callbacks.REPOST;
    }

    @Override
    public void handle(CallbackQuery query) {
        val trackId = Callbacks.getArgument(query.getData());
        val track = callbackCache.get(trackId);

        if (track == null) {
            platform.answerQuery(
                    query.getId(),
                    "⚠️ Трек уже недоступен."
            );
            return;
        }

        platform.answerQuery(
                query.getId(),
                "⏳ Начинаю скачивание..."
        );

        val resultDownload = ytDlpService.downloadAudio(track);

        platform.execute(
                SendAudioMethod.builder()
                        .chatId(query.getFrom().getId())
                        .caption(TEMPLATE_MESSAGE.formatted(track.artists(), track.getSongName()))
                        .audio(resultDownload.audio())
                        .thumbnail(resultDownload.thumbnail())
                        .build()
        );
    }
}
