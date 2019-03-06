package ru.vldf.sportsportal.service.security.userdetails;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
public interface UserDetailsProvider {

    /**
     * Returns user details by user credentials.
     *
     * @param username the user username.
     * @param password the user password.
     * @return user details.
     * @throws UsernameNotFoundException if such user does not exist.
     * @throws BadCredentialsException   if password is wrong.
     */
    IdentifiedUserDetails authorization(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

    /**
     * Returns user details by user identifier.
     *
     * @param userId the user identifier.
     * @return user details.
     * @throws UsernameNotFoundException if such user does not exist.
     */
    IdentifiedUserDetails authorization(Integer userId) throws UsernameNotFoundException;
}
