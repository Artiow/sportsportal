package ru.vldf.sportsportal.integration.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @author Namednev Artem
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;


    public MailSender sender() {
        return new MailSender();
    }


    public class MailSender {

        private MimeMessage message;
        private MimeMessageHelper helper;


        private MailSender() {
            message = javaMailSender.createMimeMessage();
            helper = new MimeMessageHelper(message);
            helper.setValidateAddresses(true);
        }


        public MailSender setFrom(String from, String personal) throws MessagingException {
            try {
                helper.setFrom(from, personal);
                return this;
            } catch (UnsupportedEncodingException e) {
                throw new MessagingException("Can't set personal", e);
            }
        }

        public MailSender setTo(String to) throws MessagingException {
            helper.setTo(to);
            return this;
        }

        public MailSender setCc(String[] cc) throws MessagingException {
            helper.setCc(cc);
            return this;
        }

        public MailSender setBcc(String[] bcc) throws MessagingException {
            helper.setBcc(bcc);
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
