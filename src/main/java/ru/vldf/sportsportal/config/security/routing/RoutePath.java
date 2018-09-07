package ru.vldf.sportsportal.config.security.routing;

public class RoutePath {

    private String pattern;
    private String httpMethod;


    public String getPattern() {
        return pattern;
    }

    public RoutePath setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public RoutePath setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }
}
