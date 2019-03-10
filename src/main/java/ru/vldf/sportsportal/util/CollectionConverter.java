package ru.vldf.sportsportal.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
public final class CollectionConverter {

    public static <S, R> List<R> convertToList(Collection<S> source, Function<? super S, ? extends R> mapper) {
        return convert(source, mapper, Collectors.toList());
    }

    public static <S, R> Set<R> convertToSet(Collection<S> source, Function<? super S, ? extends R> mapper) {
        return convert(source, mapper, Collectors.toSet());
    }

    public static <S, R, C> C convert(
            Collection<S> source,
            Function<? super S, ? extends R> mapper,
            Collector<? super R, ?, C> collector
    ) {
        return source.stream().map(mapper).collect(collector);
    }
}
