package ru.vldf.sportsportal.config.messages;

import org.springframework.context.support.MessageSourceAccessor;

import javax.validation.constraints.NotNull;

/**
 * @author Namednev Artem
 */
public interface MessageContainer {

    MessageSourceAccessor getAccessor();

    String get(@NotNull String msg);

    String get(@NotNull String msg, Object... args);
}
