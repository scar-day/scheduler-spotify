package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import dev.scarday.telegramspotify.utility.FileNameUtility;
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
    String performer;
    String title;

    @Override
    public SendAudio toApiMethod(KeyboardMapper keyboardMapper) {
        val audioInputFile = new InputFile(
                new ByteArrayInputStream(audio),
                FileNameUtility.audioFileName(performer, title)
        );

        val send = SendAudio.builder()
                .audio(audioInputFile)
                .performer(performer)
                .title(title)
                .caption(text)
                .chatId(chatId);

        if (thumbnail != null && thumbnail.length > 0) {
            send.thumbnail(new InputFile(
                    new ByteArrayInputStream(thumbnail),
                    "cover.jpg"
            ));
        }

        if (keyboard != null) {
            send.replyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return send.build();
    }
}

