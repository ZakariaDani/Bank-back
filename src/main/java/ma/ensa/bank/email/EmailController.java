package ma.ensa.bank.email;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @PostMapping(path = "/email")
    public void sendMail(@RequestBody EmailEntity emailEntity) {
        emailService.sendMail(emailEntity);
    }

}