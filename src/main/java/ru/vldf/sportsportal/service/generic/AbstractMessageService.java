package ru.vldf.sportsportal.service.generic;

import ru.vldf.sportsportal.config.messages.MessageContainer;

public abstract class AbstractMessageService {

    private MessageContainer messages;

    protected void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    protected String mGet(String msg) {
        return messages.get(msg);
    }

    protected String mGetAndFormat(String msg, Object... args) {
        return messages.getAndFormat(msg, args);
    }
}
