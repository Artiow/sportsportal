package ru.vldf.sportsportal.config.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * @author Namednev Artem
 */
@Configuration
public class MessageConfig implements MessageContainer {

    private final Locale locale;
    private final MessageSource source;
    private final MessageSourceAccessor accessor;


    @Autowired
    public MessageConfig(
            @Value("${locale.country}") String localeCountry,
            @Value("${locale.language}") String localeLanguage,
            MessageSource messageSource
    ) {
        this.source = messageSource;
        this.locale = new Locale(localeLanguage, localeCountry);
        this.accessor = new MessageSourceAccessor(this.source, this.locale);
    }


    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(this.source);
        return factory;
    }


    @Override
    public Locale getLocale() {
        return locale;
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
    public String get(@NotNull String msg, Object... args) {
        return String.format(get(msg), args);
    }
}
