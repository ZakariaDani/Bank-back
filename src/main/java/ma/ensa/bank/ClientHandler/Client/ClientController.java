package ma.ensa.bank.ClientHandler.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @CrossOrigin
    @GetMapping("/clients")
    public List<Client> getClients(){
        return clientService.getClients();
    }

    @CrossOrigin
    @PostMapping("/addclient")
    public void addClient(@RequestBody Client client){
        if(client==null){
            throw new IllegalStateException("No client to add");
        }else{
            clientService.addClient(client);
        }
    }

    @CrossOrigin
    @PutMapping(value="/updateclient/{ClientId}")
    public void updateClient(@PathVariable("ClientId") Long ClientId,@RequestBody Client client){
        if(client==null){
            throw new IllegalStateException("No client to update");
        }else{
            clientService.updateClient(ClientId,client);
        }
    }

    @CrossOrigin
    @DeleteMapping(value="/deleteclient/{clientid}")
    public void deleteagent(@PathVariable("clientid") Long clientid){
        if(clientid==null){
            throw new IllegalStateException("Please Enter a valid CardId");
        }else {
            clientService.deleteAgent(clientid);
        }
    }
}
