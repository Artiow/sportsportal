package ru.vldf.sportsportal.config.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * @author Namednev Artem
 */
@Configuration
public class MessageConfig implements MessageContainer {

    private final MessageSource source;
    private final MessageSourceAccessor accessor;


    @Autowired
    public MessageConfig(
            @Value("${locale.country}") String localeCountry,
            @Value("${locale.language}") String localeLanguage,
            MessageSource messageSource
    ) {
        this.accessor = new MessageSourceAccessor(this.source = messageSource, new Locale(localeLanguage, localeCountry));
    }


    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(this.source);
        return factory;
    }


    @Override
    public MessageSourceAccessor getAccessor() {
        return this.accessor;
    }

    @Override
    public String get(String msg) {
        try {
            Assert.hasText(msg, "message code must be not blank");
            return getAccessor().getMessage(msg.trim());
        } catch (NoSuchMessageException e) {
            return '{' + msg + '}';
        }
    }

    @Override
    public String get(@NotNull String msg, Object... args) {
        Assert.noNullElements(args, "message args must be not null");
        return String.format(get(msg), args);
    }
}
