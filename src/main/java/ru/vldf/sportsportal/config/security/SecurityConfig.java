package ru.vldf.sportsportal.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationFilter;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationProvider;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationFilter;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationProvider;
import ru.vldf.sportsportal.config.security.routing.RightsDifferentiationRouter;
import ru.vldf.sportsportal.controller.advice.AdviseController;
import ru.vldf.sportsportal.service.security.AuthorizationProvider;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RightsDifferentiationRouter router;
    private final AuthorizationProvider authorizationProvider;

    private final AdviseController adviseController;
    private final MessageContainer messages;


    @Autowired
    public SecurityConfig(
            RightsDifferentiationRouter router,
            AuthorizationProvider authorizationProvider,
            AdviseController adviseController,
            MessageContainer messages
    ) {
        super();
        this.router = router;
        this.authorizationProvider = authorizationProvider;
        this.adviseController = adviseController;
        this.messages = messages;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(jwtAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()

                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable();

        ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
        for (Map.Entry<String, RequestMatcher> entry : router.getSecurityRouteMap().entrySet()) {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) authorizeRequests.requestMatchers(entry.getValue())).hasRole(entry.getKey());
        }
    }


    @Bean
    public FilterRegistrationBean registrationBean(final JWTAuthenticationFilter authenticationFilter) {
        final FilterRegistrationBean<JWTAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(authenticationFilter);
        bean.setEnabled(false);
        return bean;
    }


    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() throws Exception {
        return configureProvider(new JWTAuthenticationProvider());
    }

    public <T extends AbstractTokenAuthenticationProvider> T configureProvider(T provider) {
        provider.setAuthorizationProvider(authorizationProvider);
        provider.setMessageContainer(messages);
        return provider;
    }


    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return configureFilter(new JWTAuthenticationFilter(router.getProtectedPathsRequestMatcher()));
    }

    private <T extends AbstractTokenAuthenticationFilter> T configureFilter(T filter) throws Exception {
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setMessageContainer(messages);
        return filter;
    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((httpServletRequest, httpServletResponse, s) -> {
            // no redirect
        });
        return successHandler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServletOutputStream out = response.getOutputStream();
            new ObjectMapper().writeValue(out, adviseController.warnDTO(ex, "Unauthorized access attempt"));
            out.flush();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServletOutputStream out = response.getOutputStream();
            new ObjectMapper().writeValue(out, adviseController.warnDTO(ex, "Forbidden access attempt"));
            out.flush();
        };
    }
}
