package dev.scarday.telegramspotify.telegram.callback;

import dev.scarday.telegramspotify.cache.Callbacks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackDispatcher {
    Map<String, CallbackHandler> handlers;

    public CallbackDispatcher(List<CallbackHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(
                        CallbackHandler::action,
                        Function.identity()
                ));
    }

    public void dispatch(CallbackQuery query) {
        val action = Callbacks.getAction(query.getData());

        val handler = handlers.get(action);

        if (handler == null) {
            return;
        }

        handler.handle(query);
    }
}
