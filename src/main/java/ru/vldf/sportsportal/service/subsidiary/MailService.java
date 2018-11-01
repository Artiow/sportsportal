package ru.vldf.sportsportal.service.subsidiary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
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
