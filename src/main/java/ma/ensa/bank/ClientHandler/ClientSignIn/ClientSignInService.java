package ma.ensa.bank.ClientHandler.ClientSignIn;

import lombok.AllArgsConstructor;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientSignInService {

    private final ClientService clientService;

    public ClientDTO login(Client client) {
        return clientService.SignIn(client);
}

}

