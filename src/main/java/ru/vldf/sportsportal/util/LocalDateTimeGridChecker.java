package ru.vldf.sportsportal.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public final class LocalDateTimeGridChecker {

    public static boolean check(Collection<LocalDateTime> dateTimeCollection, boolean halfHourAvailable, boolean fullHourRequired) {
        return check(dateTimeCollection, halfHourAvailable, fullHourRequired, true);
    }

    public static boolean check(Collection<LocalDateTime> dateTimeCollection, boolean halfHourAvailable, boolean fullHourRequired, boolean timeCheck) {
        int divider = halfHourAvailable ? 30 : 60;
        LocalDateTime now = LocalDateTime.now();

        // time validation pre-check
        for (LocalDateTime item : dateTimeCollection) {
            if ((item.getMinute() % divider) != 0) return false;
            if (!item.isAfter(now) && timeCheck) return false;
        }

        Iterator<LocalDateTime> iterator = dateTimeCollection.iterator();
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
            return !(chainLength < 2);
        } else return true;
    }

    public static List<LocalDateTime> filter(Collection<LocalDateTime> dateTimeCollection, boolean halfHourAvailable, boolean fullHourRequired) {
        return filter(dateTimeCollection, halfHourAvailable, fullHourRequired, true);
    }

    public static List<LocalDateTime> filter(Collection<LocalDateTime> dateTimeCollection, boolean halfHourAvailable, boolean fullHourRequired, boolean timeCheck) {
        List<LocalDateTime> result = new ArrayList<>(dateTimeCollection);

        int divider = halfHourAvailable ? 30 : 60;
        LocalDateTime now = LocalDateTime.now();

        // time validation pre-check
        ListIterator<LocalDateTime> listIterator = result.listIterator();
        while (listIterator.hasNext()) {
            LocalDateTime item = listIterator.next();
            if ((item.getMinute() % divider) != 0) listIterator.remove();
            if (!item.isAfter(now) && timeCheck) listIterator.remove();
        }

        listIterator = result.listIterator();
        if (halfHourAvailable && fullHourRequired && listIterator.hasNext()) {
            LocalDateTime prevItem = listIterator.next();
            LocalDateTime item;
            int chainLength = 1;
            while (listIterator.hasNext()) {
                item = listIterator.next();
                if (ChronoUnit.MINUTES.between(prevItem, item) == divider) {
                    chainLength++;
                } else {
                    if (chainLength < 2) {
                        listIterator.previous();
                        listIterator.remove();
                        listIterator.next();
                    }
                    chainLength = 1;
                }
                prevItem = item;
            }
            if (chainLength < 2) {
                listIterator.remove();
            }
        }

        return result;
    }
}
