package ru.vldf.sportsportal.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

public class LocalDateTimeNormalizer {

    public static boolean check(List<LocalDateTime> dateTimeList, boolean halfHourAvailable, boolean fullHourRequired) {
        int divider = halfHourAvailable ? 30 : 60;
        for (LocalDateTime item : dateTimeList) if ((item.getMinute() % divider) != 0) return false;
        Iterator<LocalDateTime> iterator = dateTimeList.iterator();
        if (halfHourAvailable && fullHourRequired && iterator.hasNext()) {
            LocalDateTime prevItem = iterator.next();
            LocalDateTime item;
            int chainLength = 1;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (ChronoUnit.MINUTES.between(prevItem, item) == divider) {
                    chainLength++;
                } else {
                    if (chainLength < 2) return false;
                    chainLength = 1;
                }
                prevItem = item;
            }
        }
        return true;
    }
}
