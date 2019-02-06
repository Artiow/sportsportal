package ru.vldf.sportsportal.config.security.routing;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class RoutePath {

    private String pattern;
    private String httpMethod;
}
