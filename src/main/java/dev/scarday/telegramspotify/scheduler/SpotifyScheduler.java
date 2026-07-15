package dev.scarday.telegramspotify.scheduler;

import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.configuration.TelegramConfiguration;
import dev.scarday.telegramspotify.service.SpotifyService;
import dev.scarday.telegramspotify.telegram.message.impl.EditMessageTextMethod;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import dev.scarday.telegramspotify.telegram.message.keyboard.factory.SimpleButtonFactory;
import dev.scarday.telegramspotify.telegram.message.status.TrackStatusFormatterRegistry;
import dev.scarday.telegramspotify.telegram.platform.TelegramPlatform;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    TrackStatusFormatterRegistry statusFormatterRegistry;

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void refreshSpotifyStatus() {
        try {
            val track = spotifyService.getCurrentTrack();

            Keyboard keyboard = null;

            if (track != null && track.getId() != null) {
                callbackCache.register(track);

                keyboard = Keyboard.builder()
                        .row(simpleButtonFactory.createDownloadChoiceButton(track.getId()))
                        .row(simpleButtonFactory.createSourceCodeButton())
                        .build();
            }

            platform.execute(EditMessageTextMethod.builder()
                    .chatId(telegramConfiguration.getMessage().getChatId())
                    .messageId(telegramConfiguration.getMessage().getId())
                    .keyboard(keyboard)
                    .text(statusFormatterRegistry.format(track))
                    .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса в шедулере: {}", e.getMessage(), e);
        }
    }
}
