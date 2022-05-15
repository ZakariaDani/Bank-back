package ma.ensa.bank.ClientHandler.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;


    @CrossOrigin
    @PostMapping("/register")
    public void addClient(@RequestBody Client client){
        System.out.println("hol");
        client.setBirth(LocalDate.of(2000,9,14));
        if(client == null){
            throw new IllegalStateException("tous les champs doivent etre remplis");
        }
        clientService.addClient(client);
    }

    @CrossOrigin
    @PutMapping(value="/updateClient/{email}")
    public void updateClient(@PathVariable("email") String email, @RequestBody Client client){
        clientService.updateClient(email,client);
    }
}
