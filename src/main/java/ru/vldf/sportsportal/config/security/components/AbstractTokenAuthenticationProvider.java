package ru.vldf.sportsportal.config.security.components;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.service.security.AuthorizationProvider;

/**
 * @author Namednev Artem
 */
public abstract class AbstractTokenAuthenticationProvider<T extends UsernamePasswordAuthenticationToken> extends AbstractUserDetailsAuthenticationProvider {

    private final Class<T> type;

    protected MessageContainer messageContainer;
    protected AuthorizationProvider provider;


    /**
     * Abstract token authentication provider constructor.
     *
     * @param type the token type.
     */
    protected AbstractTokenAuthenticationProvider(Class<T> type) {
        Assert.notNull(type, "Token type schema must be not null");
        this.type = type;
    }


    public void setMessageContainer(MessageContainer messageContainer) {
        Assert.notNull(messageContainer, "Message container cannot be null");
        this.messageContainer = messageContainer;
        this.messages = messageContainer.getAccessor();
    }

    public void setAuthorizationProvider(AuthorizationProvider provider) {
        Assert.notNull(provider, "Authorization provider cannot be null");
        this.provider = provider;
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        super.doAfterPropertiesSet();
        Assert.notNull(messageContainer, "Message container must be specified");
        Assert.notNull(messages, "Message source accessor must be specified");
        Assert.notNull(provider, "Authorization provider must be specified");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (type.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
        // no additional checks
    }

    /**
     * Retrieve user by it's username and authentication token.
     *
     * @param username the authentication username (not used).
     * @param auth     the authentication token.
     * @return retrieved user details.
     * @throws AuthenticationException if authentication failed.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
        Assert.isInstanceOf(type, auth);
        return retrieveUser((T) auth);
    }

    /**
     * Retrieve user by user authentication token.
     *
     * @param auth the authentication token.
     * @return retrieved user details.
     * @throws AuthenticationException if authentication failed.
     */
    protected abstract UserDetails retrieveUser(T auth) throws AuthenticationException;
}
