package ma.ensa.bank.email;

import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;


    public EmailService() {
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(EmailEntity emailEntity) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailEntity.getReceiver());
            simpleMailMessage.setText(emailEntity.getMsg());
            simpleMailMessage.setSubject(emailEntity.getSubject());

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }





}