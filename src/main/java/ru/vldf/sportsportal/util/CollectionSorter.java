package ru.vldf.sportsportal.util;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Namednev Artem
 */
public final class CollectionSorter {

    public static <T extends Comparable<? super T>> List<T> getSorted(Collection<? extends T> source) {
        Assert.notNull(source, "source must be present");
        List<T> newList = new ArrayList<>(source);
        Collections.sort(newList);
        return newList;
    }
}
