package ru.vldf.sportsportal.util;

import java.time.LocalDateTime;
import java.util.Collection;

public class LocalDateTimeNormalizer {

    public static boolean check(Collection<LocalDateTime> list, boolean half) {
        int divider = half ? 30 : 60;
        for (LocalDateTime item : list) {
            if ((item.getMinute() % divider) != 0) {
                return false;
            }
        }

        return true;
    }
}
