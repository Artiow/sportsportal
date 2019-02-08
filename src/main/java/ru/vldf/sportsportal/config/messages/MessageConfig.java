package ru.vldf.sportsportal.config.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * @author Namednev Artem
 */
@Configuration
public class MessageConfig implements MessageContainer {

    private MessageSource source;
    private MessageSourceAccessor accessor;
    private String localeLanguage;
    private String localeCountry;
    private Locale locale;

    @Autowired
    public MessageConfig(MessageSource messageSource) {
        this.source = messageSource;
    }

    @Value("${locale.language}")
    public void setLocaleLanguage(String localeLanguage) {
        this.localeLanguage = localeLanguage;
    }

    @Value("${locale.country}")
    public void setLocaleCountry(String localeCountry) {
        this.localeCountry = localeCountry;
    }


    /**
     * Accessor configuration.
     */
    @PostConstruct
    private void init() {
        this.locale = new Locale(localeLanguage, localeCountry);
        this.accessor = new MessageSourceAccessor(this.source, this.locale);
    }

    /**
     * Message source for validation messages configuration.
     *
     * @return {@link Validator} bean
     */
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
