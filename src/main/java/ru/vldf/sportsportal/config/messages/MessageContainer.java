package ru.vldf.sportsportal.config.messages;

import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * @author Namednev Artem
 */
public interface MessageContainer {

    Locale getLocale();

    String get(@NotNull String msg);

    String get(@NotNull String msg, Object... args);
}
