package ru.vldf.sportsportal.config.security.routing;

import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Map;

/**
 * @author Namednev Artem
 */
public interface RightsDifferentiationRouter {

    Map<String, RequestMatcher> getSecurityRouteMap();

    RequestMatcher getProtectedPathsRequestMatcher();

    RequestMatcher getPublicPathsRequestMatcher();

    RequestMatcher getLoginPathRequestMatcher();

    String getLoginPath();
}
