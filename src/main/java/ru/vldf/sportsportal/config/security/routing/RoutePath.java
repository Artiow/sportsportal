package ru.vldf.sportsportal.config.security.routing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "routePath")
@XmlAccessorType(XmlAccessType.FIELD)
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
