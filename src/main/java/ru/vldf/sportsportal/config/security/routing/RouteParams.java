package ru.vldf.sportsportal.config.security.routing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class RouteParams {

    private List<RoutePath> publicRoutePaths;
    private Map<String, List<RoutePath>> protectedRoutePaths;
}
