package ru.vldf.sportsportal.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * @author Namednev Artem
 */
public final class SimpleGrantedAuthorityBuilder {

    public static SimpleGrantedAuthority of(String role) {
        Assert.hasText(role, "A role textual representation is required");
        String rawRole = role.trim().toUpperCase();
        Assert.isTrue(!rawRole.startsWith("ROLE_"), () -> rawRole + " cannot start with ROLE_ (it is automatically added)");
        return new SimpleGrantedAuthority("ROLE_" + rawRole);
    }
}
