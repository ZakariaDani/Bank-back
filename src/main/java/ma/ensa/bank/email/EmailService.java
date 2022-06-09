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
    private final JavaMailSender javaMailSender;
    @Autowired
    private ClientService clientService;

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

    public String sendCodeToClient(Long id){
        Optional<Client> c = clientService.getClientById(id);
        if(c.isPresent()){
            Client client  = c.get();
            String password = new Random().ints(10, 33, 122).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            client.setPassword(password);
            clientService.updateClient(id, client);
            String message = "Hello Mr" + client.getFirstName() + client.getLastName() + "Your New Password is: " + client.getPassword();
            EmailEntity emailEntity = new EmailEntity(client.getEmail(), "your New Password", message);
            try {
                this.sendMail(emailEntity);
                return "Succes";
            }catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
        }
        return "Error";
    }



}
