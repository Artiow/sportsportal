package ru.vldf.sportsportal.config.security.routing;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.web.util.matcher.*;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.*;

@Configuration
public class RightsDifferentiationConfig implements RightsDifferentiationRouter {

    @Value("classpath:routing.xml")
    private Resource routeResource;

    private RequestMatcher publicRequests;
    private RequestMatcher protectedRequests;
    private Map<String, RequestMatcher> routeMap;


    @Autowired
    @PostConstruct
    private void setRouteParams() throws Exception {
        Unmarshaller unmarshaller = routeUnmarshaller();
        RouteParams params = (RouteParams) unmarshaller.unmarshal(routeResource.getInputStream());

        // public path config
        List<RoutePath> publicPaths = params.getPublicRoutePathsList().getRoutePathList();
        List<RequestMatcher> publicRequests = new ArrayList<>(publicPaths.size());
        for (RoutePath path : publicPaths) {
            publicRequests.add(new AntPathRequestMatcher(path.getPattern(), path.getHttpMethod()));
        }
        this.publicRequests = new OrRequestMatcher(publicRequests);

        // protected path config
        Map<String, RouteParams.RoutePathList> protectedPaths = params.getProtectedRoutePathsMap();
        Set<Map.Entry<String, RouteParams.RoutePathList>> protectedPathEntrySet = protectedPaths.entrySet();
        this.routeMap = new HashMap<>(protectedPathEntrySet.size());
        for (Map.Entry<String, RouteParams.RoutePathList> entry : protectedPaths.entrySet()) {
            List<RoutePath> rolePaths = entry.getValue().getRoutePathList();
            List<RequestMatcher> roleRequests = new ArrayList<>(rolePaths.size());
            for (RoutePath path : rolePaths) {
                roleRequests.add(new AntPathRequestMatcher(path.getPattern(), path.getHttpMethod()));
            }
            this.routeMap.put(entry.getKey(), new OrRequestMatcher(roleRequests));
        }

        // protected path config
        this.protectedRequests = new AndRequestMatcher(
                new NegatedRequestMatcher(this.publicRequests),
                new OrRequestMatcher(Lists.newArrayList(this.routeMap.values()))
        );
    }


    @Bean
    public Unmarshaller routeUnmarshaller() throws JAXBException {
        return JAXBContext.newInstance(
                RouteParams.class, RouteParams.RoutePathList.class, RoutePath.class
        ).createUnmarshaller();
    }

    @Override
    public Map<String, RequestMatcher> getSecurityRouteMap() {
        return routeMap;
    }

    @Override
    public RequestMatcher getAllProtectedPathRequestMatcher() {
        return protectedRequests;
    }

    @Override
    public RequestMatcher getAllPublicPathRequestMatcher() {
        return publicRequests;
    }
}
