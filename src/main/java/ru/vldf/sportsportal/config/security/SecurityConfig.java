package ru.vldf.sportsportal.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import ru.vldf.sportsportal.controller.advice.AdviseController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements RightsDifferentiationRouting {

    private final AdviseController adviseController;

    private final TokenAuthenticationProvider provider;

    @Autowired
    public SecurityConfig(TokenAuthenticationProvider provider, AdviseController adviseController) {
        super();
        this.adviseController = adviseController;
        this.provider = provider;
    }


    /**
     * Configure HttpSecurity.
     *
     * @param http - security configuration object
     * @throws Exception - if something goes wrong
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(provider)
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()

                .authorizeRequests() // todo: put here rights differentiation configuration
                .anyRequest().permitAll()
                .and()

                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable();
    }


    /**
     * FilterRegistrationBean configuration.
     * Disable Spring Boot automatic filter registration.
     *
     * @param authenticationFilter - custom filter
     * @return registration bean
     */
    @Bean
    public FilterRegistrationBean registrationBean(final TokenAuthenticationFilter authenticationFilter) {
        final FilterRegistrationBean<TokenAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(authenticationFilter);
        bean.setEnabled(false);

        return bean;
    }

    /**
     * TokenAuthenticationFilter configuration.
     *
     * @return filter bean
     * @throws Exception - if something goes wrong
     */
    @Bean
    public TokenAuthenticationFilter authenticationFilter()
            throws Exception {

        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());

        return filter;
    }

    /**
     * AuthenticationSuccessHandler configuration.
     *
     * @return success handler bean
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
     *
     * @return failure handler bean
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
     *
     * @return handler bean
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

    /**
     * BCryptPasswordEncoder configuration.
     *
     * @return password encoder bean
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
