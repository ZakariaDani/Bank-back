package ma.ensa.bank.agentHandler.agent;


import lombok.AllArgsConstructor;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/agent/")
public class AgentContorller {
    private final ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getClients(){
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @PostMapping("/addclient")
    public ResponseEntity<?> addClient(@RequestBody ClientDTO client){
        if(client==null){
            return ResponseEntity.badRequest().body("This client is not valid");
        }
        Client newClient = clientService.addClient(client);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newClient);
    }

    @GetMapping("getclient/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") final Long id) {
        Optional<Client> client = clientService.getClientById(id);
        if(client.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(client.get());
        } else {
            return ResponseEntity.badRequest().body("There is no client with that specific id");
        }
    }

    @PutMapping(value="/updateclient/{ClientId}")
    public ResponseEntity<?> updateClient(@PathVariable("ClientId") Long ClientId,@RequestBody Client client){
        if(client==null){
            return ResponseEntity.badRequest().body("The provided client is not valid");
        }else if(clientService.getClientById(ClientId).isPresent()){
            Client newClient = clientService.updateClient(ClientId, client);
            return ResponseEntity
                    .ok()
                    .body(newClient);
        }
        else{
            return ResponseEntity.badRequest().body("The provided id is not valid");
        }
    }

    @DeleteMapping(value="/deleteclient/{clientid}")
    public ResponseEntity<?> deleteagent(@PathVariable("clientid") Long clientid){
        if(clientid==null){
            return ResponseEntity.badRequest().body("The provided id is not valid");
        }
        if(clientService.getClientById(clientid).isPresent()){
            clientService.deleteClient(clientid);
            return ResponseEntity.ok().body("client delected succesfully");
        }else {
            return ResponseEntity.badRequest().body("Id doesn't exist!!");
        }
    }
}
