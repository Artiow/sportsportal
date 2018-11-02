package ru.vldf.sportsportal.config.messages;

import javax.validation.constraints.NotNull;
import java.util.Locale;

public interface MessageContainer {

    Locale getLocale();

    String get(@NotNull String msg);

    String getAndFormat(@NotNull String msg, Object... args);
}
