package ma.ensa.bank.ClientHandler.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @CrossOrigin
    @PostMapping("/addclient")
    public void addClient(@RequestBody Client client){
        if(client==null){
            throw new IllegalStateException("All Information Are Required");
        }else{
            clientService.addClient(client);
        }
    }

    @CrossOrigin
    @PutMapping(value="/updateClient/{ClientCardId}")
    public void updateClient(@PathVariable("ClientCardId") String ClientCardId, @RequestBody Client client){
        clientService.updateClient(ClientCardId,client);
    }
}
