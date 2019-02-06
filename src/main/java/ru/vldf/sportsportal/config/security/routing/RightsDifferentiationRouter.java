package ru.vldf.sportsportal.config.security.routing;

import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Map;

/**
 * @author Namednev Artem
 */
public interface RightsDifferentiationRouter {

    Map<String, RequestMatcher> getSecurityRouteMap();

    RequestMatcher getAllProtectedPathRequestMatcher();

    RequestMatcher getAllPublicPathRequestMatcher();
}
