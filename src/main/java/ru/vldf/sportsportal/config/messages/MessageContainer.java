package ru.vldf.sportsportal.config.messages;

import javax.validation.constraints.NotNull;

public interface MessageContainer {

    String get(@NotNull String msg);

    String getAndFormat(@NotNull String msg, Object... args);
}
