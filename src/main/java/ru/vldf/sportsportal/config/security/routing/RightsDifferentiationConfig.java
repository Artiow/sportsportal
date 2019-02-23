package ru.vldf.sportsportal.config.security.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.web.util.matcher.*;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Namednev Artem
 */
@Configuration
public class RightsDifferentiationConfig implements RightsDifferentiationRouter {

    @Value("classpath:secure-routing.yaml")
    private Resource routeResource;

    private RequestMatcher publicRequests;
    private RequestMatcher protectedRequests;
    private Map<String, RequestMatcher> routeMap;


    @PostConstruct
    private void setRouteParams() throws Exception {

        // yaml mapper init
        RouteParams params = new ObjectMapper(new YAMLFactory()).readValue(routeResource.getInputStream(), RouteParams.class);

        // public path config
        List<RoutePath> publicPaths = params.getPublicRoutePaths();
        List<RequestMatcher> publicRequests = new ArrayList<>(publicPaths.size());
        for (RoutePath path : publicPaths) {
            publicRequests.add(new AntPathRequestMatcher(path.getPattern(), path.getHttpMethod()));
        }
        this.publicRequests = new OrRequestMatcher(publicRequests);

        // protected path config
        Map<String, List<RoutePath>> protectedPaths = params.getProtectedRoutePaths();
        Set<Map.Entry<String, List<RoutePath>>> protectedPathEntrySet = protectedPaths.entrySet();
        this.routeMap = new HashMap<>(protectedPathEntrySet.size());
        for (Map.Entry<String, List<RoutePath>> entry : protectedPaths.entrySet()) {
            List<RoutePath> rolePaths = entry.getValue();
            List<RequestMatcher> roleRequests = new ArrayList<>(rolePaths.size());
            for (RoutePath path : rolePaths) {
                roleRequests.add(new AntPathRequestMatcher(path.getPattern(), path.getHttpMethod()));
            }
            this.routeMap.put(entry.getKey(), new AndRequestMatcher(
                    new NegatedRequestMatcher(this.publicRequests),
                    new OrRequestMatcher(roleRequests))
            );
        }

        // protected path config
        this.protectedRequests = new AndRequestMatcher(
                new NegatedRequestMatcher(this.publicRequests),
                new OrRequestMatcher(Lists.newArrayList(this.routeMap.values()))
        );
    }


    @Override
    public Map<String, RequestMatcher> getSecurityRouteMap() {
        return routeMap;
    }

    @Override
    public RequestMatcher getProtectedPathsRequestMatcher() {
        return protectedRequests;
    }

    @Override
    public RequestMatcher getPublicPathsRequestMatcher() {
        return publicRequests;
    }
}
