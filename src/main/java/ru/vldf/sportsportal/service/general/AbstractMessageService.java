package ru.vldf.sportsportal.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import ru.vldf.sportsportal.config.messages.MessageContainer;

/**
 * @author Namednev Artem
 */
public abstract class AbstractMessageService {

    @Autowired
    private MessageContainer messages;

    protected String msg(String msg) {
        return messages.get(msg);
    }

    protected String msg(String msg, Object... args) {
        return messages.get(msg, args);
    }
}
