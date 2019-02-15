package ru.vldf.sportsportal.integration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Namednev Artem
 */
@Service
@SuppressWarnings("ALL")
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String sender;


    @Autowired
    public MailService(
            JavaMailSender javaMailSender,
            @Value("${spring.mail.username}") String sender
    ) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }


    public MailSender sender() throws MessagingException {
        return new MailSender();
    }


    public class MailSender {

        private MimeMessage message;
        private MimeMessageHelper helper;


        private MailSender() throws MessagingException {
            message = javaMailSender.createMimeMessage();
            helper = new MimeMessageHelper(message);
            helper.setFrom(sender);
        }


        public MailSender setDestination(String destination) throws MessagingException {
            helper.setTo(destination);
            return this;
        }

        public MailSender setSubject(String subject) throws MessagingException {
            helper.setSubject(subject);
            return this;
        }

        public MailSender setHtml(String html) throws MessagingException {
            helper.setText(html, true);
            return this;
        }


        public void send() {
            javaMailSender.send(message);
        }
    }
}
