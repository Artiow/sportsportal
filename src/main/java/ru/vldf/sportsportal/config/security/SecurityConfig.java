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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.config.security.routing.RightsDifferentiationRouter;
import ru.vldf.sportsportal.controller.advice.AdviseController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RightsDifferentiationRouter router;
    private final TokenAuthenticationProvider provider;

    private final AdviseController adviseController;
    private final MessageContainer messages;


    @Autowired
    public SecurityConfig(
            RightsDifferentiationRouter router,
            TokenAuthenticationProvider provider,
            AdviseController adviseController,
            MessageContainer messages
    ) {
        super();
        this.router = router;
        this.provider = provider;
        this.adviseController = adviseController;
        this.messages = messages;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(provider)
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()

                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable();

        ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry authorizeRequests
                = http.authorizeRequests();

        for (Map.Entry<String, RequestMatcher> entry : router.getSecurityRouteMap().entrySet()) {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) authorizeRequests
                    .requestMatchers(entry.getValue()))
                    .hasRole(entry.getKey());
        }
    }


    /**
     * FilterRegistrationBean configuration, Spring Boot automatic filter registration disabling.
     */
    @Bean
    public FilterRegistrationBean registrationBean(final TokenAuthenticationFilter authenticationFilter) {
        final FilterRegistrationBean<TokenAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(authenticationFilter);
        bean.setEnabled(false);
        return bean;
    }

    /**
     * TokenAuthenticationFilter configuration, handlers setting.
     */
    @Bean
    public TokenAuthenticationFilter authenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(router, messages);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

    /**
     * AuthenticationSuccessHandler configuration.
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((httpServletRequest, httpServletResponse, s) -> {
            // no redirect
        });
        return successHandler;
    }

    /**
     * AuthenticationFailureHandler configuration.
     */
    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServletOutputStream out = response.getOutputStream();
            new ObjectMapper().writeValue(out, adviseController.warnDTO(ex, "Unauthorized Access Attempt."));
            out.flush();
        };
    }

    /**
     * AccessDeniedHandler configuration.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServletOutputStream out = response.getOutputStream();
            new ObjectMapper().writeValue(out, adviseController.warnDTO(ex, "Forbidden Access Attempt."));
            out.flush();
        };
    }
}
