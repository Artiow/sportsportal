package ru.vldf.sportsportal.config.security.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.web.util.matcher.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
@Component
public class RightsDifferentiationConfigurator implements RightsDifferentiationRouter {

    private final String loginRequestPath;
    private final String refreshRequestPath;
    private final RequestMatcher loginRequestMatcher;
    private final RequestMatcher refreshRequestMatcher;
    private final RequestMatcher publicRequestMatcher;
    private final RequestMatcher protectedRequestMatcher;
    private final Map<String, RequestMatcher> routeMap;


    public RightsDifferentiationConfigurator(@Value("classpath:secure-routing.yaml") Resource routeResource) throws IOException {
        RouteParams params = new ObjectMapper(new YAMLFactory()).readValue(routeResource.getInputStream(), RouteParams.class);

        // login routing config
        RoutePath loginRoute = params.getLoginRoute();
        loginRequestPath = loginRoute.getPattern();
        loginRequestMatcher = toRequestMatcher(loginRoute);

        // refresh routing config
        RoutePath refreshRoute = params.getRefreshRoute();
        refreshRequestPath = refreshRoute.getPattern();
        refreshRequestMatcher = toRequestMatcher(refreshRoute);

        // public routing config
        publicRequestMatcher = new OrRequestMatcher(loginRequestMatcher, refreshRequestMatcher, toRequestMatcher(params.getPublicRoutes()));

        // security routing config
        routeMap = params.getProtectedRoutes().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, entry -> new AndRequestMatcher(new NegatedRequestMatcher(publicRequestMatcher), toRequestMatcher(entry.getValue())))
        );

        // protected routing config
        this.protectedRequestMatcher = new OrRequestMatcher(Lists.newArrayList(this.routeMap.values()));
    }

    private RequestMatcher toRequestMatcher(RoutePath routePath) {
        return new AntPathRequestMatcher(routePath.getPattern(), routePath.getHttpMethod());
    }

    private RequestMatcher toRequestMatcher(List<RoutePath> routePaths) {
        return new OrRequestMatcher(routePaths.stream().map(this::toRequestMatcher).collect(Collectors.toList()));
    }


    @Override
    public Map<String, RequestMatcher> getSecurityRouteMap() {
        return routeMap;
    }

    @Override
    public RequestMatcher getProtectedPathsRequestMatcher() {
        return protectedRequestMatcher;
    }

    @Override
    public RequestMatcher getPublicPathsRequestMatcher() {
        return publicRequestMatcher;
    }

    @Override
    public RequestMatcher getLoginPathRequestMatcher() {
        return loginRequestMatcher;
    }

    @Override
    public RequestMatcher getRefreshPathRequestMatcher() {
        return refreshRequestMatcher;
    }

    @Override
    public String getLoginPath() {
        return loginRequestPath;
    }

    @Override
    public String getRefreshPath() {
        return refreshRequestPath;
    }
}
