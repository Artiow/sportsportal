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

    private RoutePath loginRoute;
    private RoutePath refreshRoute;
    private List<RoutePath> publicRoutes;
    private Map<String, List<RoutePath>> protectedRoutes;
}
