package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SendAudioMethod extends MessageMapper {
    byte[] audio;
    byte[] thumbnail;

    @Override
    public SendAudio toApiMethod(KeyboardMapper keyboardMapper) {
        val audioInputFile = new InputFile(
                new ByteArrayInputStream(audio),
                System.currentTimeMillis() + ".mp3"
        );

        val thumbnailInputFile = new InputFile(
                new ByteArrayInputStream(thumbnail),
                "cover.mp3"
        );

        val send = SendAudio.builder()
                .audio(audioInputFile)
                .thumbnail(thumbnailInputFile)
                .caption(text)
                .chatId(chatId);

        if (keyboard != null) {
            send.replyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return send.build();
    }
}