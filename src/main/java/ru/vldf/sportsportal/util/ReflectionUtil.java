package ru.vldf.sportsportal.util;

import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;

/**
 * @author Artem Namednev
 */
public final class ReflectionUtil {

    @SneakyThrows(NoSuchMethodException.class)
    public static MethodParameter methodParameter(Class<?> service, String name, Class<?>[] parameters, int index) {
        return new MethodParameter(service.getMethod(name, parameters), index);
    }
}
