package dev.scarday.telegramspotify.telegram.callback.impl;

import dev.scarday.telegramspotify.callback.CallbackCache;
import dev.scarday.telegramspotify.callback.Callbacks;
import dev.scarday.telegramspotify.model.SpotifyTrack;
import dev.scarday.telegramspotify.telegram.callback.CallbackHandler;
import dev.scarday.telegramspotify.telegram.keyboard.constants.ButtonStyle;
import dev.scarday.telegramspotify.telegram.message.impl.SendMessageMethod;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import dev.scarday.telegramspotify.telegram.message.keyboard.button.impl.CallbackButton;
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
public class DownloadChoiceCallbackHandler implements CallbackHandler {
    private static final String DEFAULT_MESSAGE = "Что скачиваем?";

    CallbackCache callbackCache;
    TelegramPlatform platform;

    @Override
    public String action() {
        return Callbacks.CHOICE;
    }

    @Override
    public void handle(CallbackQuery query) {
        val trackId = Callbacks.getArgument(query.getData());
        val queryId = query.getId();
        val track = callbackCache.get(trackId);

        platform.answerQuery(queryId, "");

        val keyboard = Keyboard.builder()
                .row(
                    CallbackButton.builder()
                        .style(ButtonStyle.GREEN)
                        .label("🎵 MP3-файл")
                        .data(trackId)
                        .action(Callbacks.MP3)
                        .build(),
                    CallbackButton.builder()
                        .style(ButtonStyle.BLUE)
                        .label("💿 Альбом")
                        .data(trackId)
                        .action(Callbacks.ALBUM)
                        .build()
                )
                .build();

        platform.execute(SendMessageMethod.builder()
                .chatId(String.valueOf(query.getFrom().getId()))
                .text(buildMessage(track))
                .keyboard(keyboard)
                .build()
        );
    }

    private String buildMessage(SpotifyTrack track) {
        if (track == null) {
            return DEFAULT_MESSAGE;
        }

        val sb = new StringBuilder();
        sb.append("🎧 *").append(track.artists()).append(" — ").append(track.getSongName()).append("*\n\n");

        if (track.getAlbumName() != null) {
            sb.append("💿 Альбом: `").append(track.getAlbumName()).append("`\n");
        }

        if (track.getAlbumType() != null) {
            sb.append("📀 Тип: `").append(albumTypeLabel(track.getAlbumType())).append("`\n");
        }

        if (track.getDurationMs() > 0) {
            sb.append("⏱ Длительность: `").append(track.getDurationFormatted()).append("`\n");
        }

        sb.append("\nЧто скачиваем?");

        return sb.toString();
    }

    private String albumTypeLabel(String albumType) {
        return switch (albumType) {
            case "album" -> "Альбом";
            case "single" -> "Сингл";
            case "compilation" -> "Сборник";
            default -> albumType;
        };
    }
}


