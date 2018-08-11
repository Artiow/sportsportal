package ru.vldf.sportsportal.config.messages;

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
public class MessageConfig implements MessageContainer {

    private MessageSource source;
    private MessageSourceAccessor accessor;

    @Autowired
    public MessageConfig(MessageSource messageSource) {
        this.source = messageSource;
    }


    /**
     * Accessor configuration.
     */
    @PostConstruct
    private void init() {
        this.accessor = new MessageSourceAccessor(this.source, Locale.getDefault());
    }

    /**
     * Message source for validation messages configuration.
     *
     * @return LocalValidatorFactoryBean bean
     */
    @Bean
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();

        factory.setValidationMessageSource(this.source);
        return factory;
    }


    @Override
    public String get(@NotNull String msg) {
        try {
            return accessor.getMessage(msg);
        } catch (NoSuchMessageException e) {
            return '{' + msg + '}';
        }
    }

    @Override
    public String getAndFormat(@NotNull String msg, Object... args) {
        return String.format(get(msg), args);
    }
}
