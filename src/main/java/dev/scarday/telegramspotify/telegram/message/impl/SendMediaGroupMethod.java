package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;

import java.io.ByteArrayInputStream;
import java.util.List;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SendMediaGroupMethod extends MessageMapper {
    List<AudioFile> audios;

    @Override
    public SendMediaGroup toApiMethod(KeyboardMapper keyboardMapper) {
        val media = audios.stream()
                .map(audio -> InputMediaAudio.builder()
                        .media(
                                new ByteArrayInputStream(audio.audio()),
                                System.currentTimeMillis() + ".mp3"
                        )
                        .thumbnail(new InputFile(
                                new ByteArrayInputStream(audio.thumbnail()),
                                "cover.jpg"
                        ))
                        .caption(text)
                        .parseMode(ParseMode.MARKDOWN)
                        .build())
                .toList();

        val send = SendMediaGroup.builder()
                .medias(media)
                .chatId(chatId);

        if (keyboard != null) {
            send.replyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return send.build();
    }

    public record AudioFile(byte[] audio, byte[] thumbnail) {}
}
