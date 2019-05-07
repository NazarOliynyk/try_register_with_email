package oktenweb.try_register_with_email.services.impl;

import oktenweb.try_register_with_email.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:application.properties")
public class MailServiceImpl implements MailService {

    // the following ling must be applied for any new google account which is used for mail-sender
    // https://myaccount.google.com/lesssecureapps?utm_source=google-account&utm_medium=web&hl=uk

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    Environment env;

    public String send(String email, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
        } catch (MessagingException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username")));
            helper.setTo(email);
            helper.setText(message,true);
            helper.setSubject("CONFIRMATION MESSAGE");
        } catch (MessagingException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        javaMailSender.send(mimeMessage);
        return "Message was sent";
    }
}

