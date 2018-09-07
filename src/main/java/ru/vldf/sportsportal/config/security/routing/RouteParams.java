package ru.vldf.sportsportal.config.security.routing;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "routeParams")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteParams {

    @XmlElement(name = "publicRoutePaths")
    private RoutePathList publicRoutePathsList;

    @XmlElementWrapper(name = "protectedRoutePaths")
    private Map<String, RoutePathList> protectedRoutePathsMap = new HashMap<>();


    public RoutePathList getPublicRoutePathsList() {
        return publicRoutePathsList;
    }

    public RouteParams setPublicRoutePathsList(RoutePathList publicRoutePathsList) {
        this.publicRoutePathsList = publicRoutePathsList;
        return this;
    }

    public Map<String, RoutePathList> getProtectedRoutePathsMap() {
        return protectedRoutePathsMap;
    }

    public RouteParams setProtectedRoutePathsMap(Map<String, RoutePathList> protectedRoutePathsMap) {
        this.protectedRoutePathsMap = protectedRoutePathsMap;
        return this;
    }


    @XmlType(name = "routePathList")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoutePathList {

        @XmlElement(name = "routePath")
        private List<RoutePath> routePathList = new ArrayList<>();


        public List<RoutePath> getRoutePathList() {
            return routePathList;
        }

        public RoutePathList setRoutePathList(List<RoutePath> routePathList) {
            this.routePathList = routePathList;
            return this;
        }
    }
}
