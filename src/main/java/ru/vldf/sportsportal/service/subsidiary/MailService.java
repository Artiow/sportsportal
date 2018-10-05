package ru.vldf.sportsportal.service.subsidiary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public MailSender sender() {
        return new MailSender();
    }


    public class MailSender {

        private String destination;
        private String subject;
        private String html;


        private MailSender() {
            destination = null;
            subject = null;
            html = null;
        }


        public MailSender setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public MailSender setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailSender setHtml(String html) {
            this.html = html;
            return this;
        }


        public void send() throws MessagingException {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
            messageHelper.setTo(destination);
            messageHelper.setSubject(subject);
            messageHelper.setText(html, true);
            javaMailSender.send(mailMessage);
        }
    }
}
