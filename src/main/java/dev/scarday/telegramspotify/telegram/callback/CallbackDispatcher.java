package dev.scarday.telegramspotify.telegram.callback;

import dev.scarday.telegramspotify.callback.Callbacks;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
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
        val userId = query.getFrom().getId();
        val callbackData = query.getData();
        val action = Callbacks.getAction(callbackData);

        log.debug("User {} clicked button - Action: {}, Data: {}", userId, action, callbackData);

        CallbackHandler handler = handlers.get(action);
        if (handler == null) {
            log.warn("No handler found for action: {}, userId: {}", action, userId);
            return;
        }

        log.debug("Dispatching to handler: {} for userId: {}", handler.getClass().getSimpleName(), userId);
        handler.handle(query);
    }
}
