package ma.ensa.bank.ClientHandler.ClientSignIn;

import lombok.AllArgsConstructor;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client/login")
@AllArgsConstructor
public class ClientSignInController {

    private ClientSignInService clientSignInService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ClientDTO login(@RequestBody Client client) {
        if(client.getPassword()=="" || client.getPhone()==""){
            throw new IllegalStateException("Please Fill All Inputs!");
        }
        return clientSignInService.login(client);
    }
}





