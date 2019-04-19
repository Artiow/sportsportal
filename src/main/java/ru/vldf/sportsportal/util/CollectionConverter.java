package ru.vldf.sportsportal.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.AbstractMap;
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

    public static <S, RK, RV> MultiValueMap<RK, RV> toMultiValueMap(Collection<S> source, Function<? super S, ? extends RK> keyMapper, Function<? super S, ? extends RV> valueMapper) {
        return new LinkedMultiValueMap<>(
                source.stream().collect(Collectors.groupingBy(keyMapper)).entrySet().stream().map(e -> new AbstractMap.SimpleEntry<RK, List<RV>>(e.getKey(), e.getValue().stream().map(valueMapper).collect(Collectors.toList()))).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
        );
    }


    public static <S, R> List<R> toList(Collection<S> source, Function<? super S, ? extends R> mapper) {
        return convert(source, mapper, Collectors.toList());
    }

    public static <S, R> Set<R> toSet(Collection<S> source, Function<? super S, ? extends R> mapper) {
        return convert(source, mapper, Collectors.toSet());
    }

    public static <S, R, C> C convert(Collection<S> source, Function<? super S, ? extends R> mapper, Collector<? super R, ?, C> collector) {
        return source.stream().map(mapper).collect(collector);
    }
}
