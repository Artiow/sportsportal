package ru.vldf.sportsportal.config.security.routing;

import java.util.List;
import java.util.Map;

public class RouteParams {

    private List<RoutePath> publicRoutePaths;
    private Map<String, List<RoutePath>> protectedRoutePaths;


    public List<RoutePath> getPublicRoutePaths() {
        return publicRoutePaths;
    }

    public RouteParams setPublicRoutePaths(List<RoutePath> publicRoutePaths) {
        this.publicRoutePaths = publicRoutePaths;
        return this;
    }

    public Map<String, List<RoutePath>> getProtectedRoutePaths() {
        return protectedRoutePaths;
    }

    public RouteParams setProtectedRoutePaths(Map<String, List<RoutePath>> protectedRoutePaths) {
        this.protectedRoutePaths = protectedRoutePaths;
        return this;
    }
}
