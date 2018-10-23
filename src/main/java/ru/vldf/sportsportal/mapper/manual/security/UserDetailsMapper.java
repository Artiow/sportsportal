package ru.vldf.sportsportal.mapper.manual.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUser;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

import java.util.Collection;
import java.util.Map;

@Component
public class UserDetailsMapper {

    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {

    };


    public Map<String, Object> toMap(IdentifiedUserDetails userDetails) {
        return mapper.convertValue(userDetails, mapTypeReference);
    }

    public IdentifiedUserDetails toIdentifiedUserDetails(Map<String, Object> map) {
        IdentifiedUserDetailsDTO dto = mapper.convertValue(map, IdentifiedUserDetailsDTO.class);
        return new IdentifiedUser(dto.getId(), User.withUserDetails(dto).build());
    }


    @SuppressWarnings("unused")
    private static class IdentifiedUserDetailsDTO implements IdentifiedUserDetails {

        private int id;
        private String password;
        private String username;
        private Collection<GrantedAuthorityDTO> authorities;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;

        @Override
        public int getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public Collection<GrantedAuthorityDTO> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(Collection<GrantedAuthorityDTO> authorities) {
            this.authorities = authorities;
        }

        @Override
        public boolean isAccountNonExpired() {
            return accountNonExpired;
        }

        public void setAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
        }

        @Override
        public boolean isAccountNonLocked() {
            return accountNonLocked;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return credentialsNonExpired;
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    @SuppressWarnings("unused")
    private static class GrantedAuthorityDTO implements GrantedAuthority {

        private String role;

        @Override
        public String getAuthority() {
            return role;
        }

        public void setAuthority(String role) {
            this.role = role;
        }
    }
}
