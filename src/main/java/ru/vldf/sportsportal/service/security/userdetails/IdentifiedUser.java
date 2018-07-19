package ru.vldf.sportsportal.service.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class IdentifiedUser extends User implements IdentifiedUserDetails {

    private final Integer id;


    public IdentifiedUser(Integer id, UserDetails u) {
        this(id, u.getUsername(), u.getPassword(), u.isEnabled(), u.isAccountNonExpired(), u.isCredentialsNonExpired(), u.isAccountNonLocked(), u.getAuthorities());
    }

    public IdentifiedUser(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public IdentifiedUser(Integer id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }


    @Override
    public Integer getId() {
        return id;
    }
}
