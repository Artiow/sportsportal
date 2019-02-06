package ru.vldf.sportsportal.service.generic;

import ru.vldf.sportsportal.config.messages.MessageContainer;

public abstract class AbstractMessageService {

    private MessageContainer messages;

    public AbstractMessageService(MessageContainer messages) {
        this.messages = messages;
    }

    public String msg(String msg) {
        return messages.get(msg);
    }

    public String msg(String msg, Object... args) {
        return messages.get(msg, args);
    }
}
