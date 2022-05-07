package ma.ensa.bank.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public String sendMail(EmailEntity emailEntity) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailEntity.getReceiver());
            simpleMailMessage.setText(emailEntity.getMsg());
            simpleMailMessage.setSubject(emailEntity.getSubject());

            javaMailSender.send(simpleMailMessage);
            return "Success";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error";
        }
    }

}
