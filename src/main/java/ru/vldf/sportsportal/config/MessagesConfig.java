package ru.vldf.sportsportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Configuration
public class MessagesConfig {

    private MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public MessagesConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Accessor configuration.
     */
    @PostConstruct
    private void init() {
        this.accessor = new MessageSourceAccessor(this.messageSource, Locale.getDefault());
    }

    /**
     * Message source configuration.
     *
     * @return LocalValidatorFactoryBean bean
     */
    @Bean
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();

        factory.setValidationMessageSource(this.messageSource);
        return factory;
    }


    public String get(@NotNull String msg) {
        try {
            return accessor.getMessage(msg);
        } catch (NoSuchMessageException e) {
            return '{' + msg + '}';
        }
    }

    public String getAndFormat(@NotNull String msg, Object... args) {
        return String.format(get(msg), args);
    }
}
