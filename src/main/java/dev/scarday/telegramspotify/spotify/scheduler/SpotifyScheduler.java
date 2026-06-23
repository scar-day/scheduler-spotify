package dev.scarday.telegramspotify.spotify.scheduler;

import dev.scarday.telegramspotify.spotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.spotify.service.SpotifyService;
import dev.scarday.telegramspotify.telegram.bot.TelegramBot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpotifyScheduler {
    SpotifyService spotifyService;
    TelegramBot telegramBot;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void refreshSpotifyStatus() {
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        try {
            val track = spotifyService.getCurrentTrack();
            val messageText = buildTelegramMessage(track);
            telegramBot.updateMessage(messageText);
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса в шедулере: {}", e.getMessage(), e);
        }
    }

    private String buildTelegramMessage(SpotifyTrack track) {
        if (track.isPlayed()) {
            return String.format(
                    """
                            [📱](tg://emoji?id=5346074681004801565) *Сейчас играет:*
                            🔥 `%s — %s`
                            
                            ⏱ Прогресс: `[%s / %s]`
                            %s
                            
                            Обновлено: %s
                            [‎](%s)""",
                    track.getArtistsList(),
                    track.getSongName(),
                    track.getProgressFormatted(),
                    track.getDurationFormatted(),
                    createProgressBar(track.getProgressMs(), track.getDurationMs()),
                    "*" + sdf.format(new Date()) + "* (GMT+3)", track.getCoverUrl()
            );
        } else if (track.isPaused()) {
            return String.format(
                    """
                            ⏸ *Музыка на паузе:*
                            💤 `%s — %s`
                            
                            ⏱ Остановлено на: `[%s / %s]`
                            
                            Обновлено: %s
                            [‎](%s)""",
                    track.getArtistsList(),
                    track.getSongName(),
                    track.getProgressFormatted(),
                    track.getDurationFormatted(),
                    "*" + sdf.format(new Date()) + "* (GMT+3)", track.getCoverUrl()
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
