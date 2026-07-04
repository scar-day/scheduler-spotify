package dev.scarday.telegramspotify.scheduler;

import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.configuration.TelegramConfiguration;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.service.SpotifyService;
import dev.scarday.telegramspotify.telegram.message.impl.EditMessageTextMethod;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import dev.scarday.telegramspotify.telegram.message.keyboard.factory.SimpleButtonFactory;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpotifyScheduler {
    SpotifyService spotifyService;
    TelegramConfiguration telegramConfiguration;
    TelegramPlatform platform;
    SimpleButtonFactory simpleButtonFactory;

    CallbackCache callbackCache;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void refreshSpotifyStatus() {
        try {
            val track = spotifyService.getCurrentTrack();

            Keyboard keyboard = null;

            if (track != null && track.getId() != null) {
                callbackCache.register(track);

                keyboard = Keyboard.builder()
                        .row(simpleButtonFactory.createDownloadButton(track.getId()))
                        .row(simpleButtonFactory.createSourceCodeButton())
                        .build();
            }

            platform.execute(EditMessageTextMethod.builder()
                    .chatId(telegramConfiguration.getMessage().getChatId())
                    .messageId(telegramConfiguration.getMessage().getId())
                    .keyboard(keyboard)
                    .text(buildTelegramMessage(track))
                    .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса в шедулере: {}", e.getMessage(), e);
        }
    }

    private String buildTelegramMessage(@Nullable SpotifyTrack track) {
        if (track != null && track.isPlayed()) {
            return String.format(
                    """
                            [📱](tg://emoji?id=5346074681004801565) *Сейчас играет:*
                            🔥 `%s — %s`
                            
                            ⏱ Прогресс: `[%s / %s]`
                            %s
                            
                            Обновлено: %s
                            [‎](%s)""",
                    track.artists(),
                    track.getSongName(),
                    track.getProgressFormatted(),
                    track.getDurationFormatted(),
                    createProgressBar(track.getProgressMs(), track.getDurationMs()),
                    "*" + FORMATTER.format(ZonedDateTime.now(MOSCOW_ZONE)) + "* (GMT+3)", track.getCoverUrl()
            );
        } else if (track != null && track.isPaused()) {
            return String.format(
                    """
                            ⏸ *Музыка на паузе:*
                            💤 `%s — %s`
                            
                            ⏱ Остановлено на: `[%s / %s]`
                            
                            Обновлено: %s
                            [‎](%s)""",
                    track.artists(),
                    track.getSongName(),
                    track.getProgressFormatted(),
                    track.getDurationFormatted(),
                    "*" + FORMATTER.format(ZonedDateTime.now(MOSCOW_ZONE)) + "* (GMT+3)", track.getCoverUrl()
            );
        } else {
            return "\uD83D\uDCA4";
        }
    }

    private String createProgressBar(long current, long total) {
        if (total <= 0) return "`──────────`";

        int totalBars = 10;
        int filledBars = (int) ((double) current / total * totalBars);

        StringBuilder sb = new StringBuilder("`");
        for (int i = 0; i < totalBars; i++) {
            if (i == filledBars) {
                sb.append("🔘");
            } else if (i < filledBars) {
                sb.append("▬");
            } else {
                sb.append("─");
            }
        }
        sb.append("`");
        return sb.toString();
    }
}
