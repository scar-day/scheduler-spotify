package dev.scarday.telegramspotify.telegram.platform;

import dev.scarday.telegramspotify.telegram.keyboard.mapper.KeyboardMapper;
import dev.scarday.telegramspotify.telegram.message.MessageMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TelegramPlatform implements Platform {
    TelegramClient telegramClient;
    KeyboardMapper keyboardMapper;

    @Override
    @SneakyThrows
    public void execute(MessageMapper messageMapper) {
        val method = messageMapper.toApiMethod(keyboardMapper);
        switch (method) {
            case SendAudio sa -> telegramClient.execute(sa);
            case EditMessageText emt -> telegramClient.execute(emt);
            case SendMediaGroup smg -> telegramClient.execute(smg);
            default -> throw new IllegalStateException("Unexpected value: " + method.getMethod());
        }
    }

    @Override
    @SneakyThrows
    public void answerQuery(String id, String message) {
        val answer = AnswerCallbackQuery.builder()
                .callbackQueryId(id)
                .text(message)
                .showAlert(false)
                .build();

        telegramClient.execute(answer);
    }
}
