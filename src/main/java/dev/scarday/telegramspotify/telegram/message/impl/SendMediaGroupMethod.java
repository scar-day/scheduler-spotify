package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import dev.scarday.telegramspotify.utility.FileNameUtility;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SendMediaGroupMethod extends MessageMapper {
    List<AudioFile> audios;

    @Override
    public SendMediaGroup toApiMethod(KeyboardMapper keyboardMapper) {
        val media = new ArrayList<InputMediaAudio>(audios.size());
        for (int i = 0; i < audios.size(); i++) {
            media.add(toMedia(audios.get(i), i == 0 ? text : null));
        }

        val send = SendMediaGroup.builder()
                .medias(media)
                .chatId(chatId);

        if (keyboard != null) {
            send.replyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return send.build();
    }

    private InputMediaAudio toMedia(AudioFile audio, String caption) {
        val media = InputMediaAudio.builder()
                .media(
                        new ByteArrayInputStream(audio.audio()),
                        FileNameUtility.audioFileName(audio.artist(), audio.title())
                )
                .performer(audio.artist())
                .title(audio.title())
                .caption(caption)
                .parseMode(ParseMode.MARKDOWN);

        if (audio.thumbnail() != null && audio.thumbnail().length > 0) {
            media.thumbnail(new InputFile(
                    new ByteArrayInputStream(audio.thumbnail()),
                    "cover.jpg"
            ));
        }

        return media.build();
    }

    public record AudioFile(String artist, String title, byte[] audio, byte[] thumbnail) {}
}
