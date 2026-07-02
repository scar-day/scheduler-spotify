package dev.scarday.telegramspotify.telegram.message.impl;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import dev.scarday.telegramspotify.telegram.message.keyboard.Keyboard;
import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendAudioMethod extends MessageMapper {
    String caption;
    long chatId;
    byte[] audio;
    byte[] thumbnail;

    @Nullable Keyboard keyboard;

    @Override
    public SendAudio toApiMethod(KeyboardMapper keyboardMapper) {
        val send = SendAudio.builder()
                .audio(new InputFile(new ByteArrayInputStream(audio), System.currentTimeMillis() + ".mp3"))
                .thumbnail(new InputFile(new ByteArrayInputStream(thumbnail), "cover.jpg"))
                .caption(caption)
                .chatId(chatId)
                .parseMode(ParseMode.MARKDOWN)
                .build();

        if (keyboard != null) {
            send.setReplyMarkup(keyboardMapper.toKeyboard(keyboard));
        }

        return send;
    }
}
