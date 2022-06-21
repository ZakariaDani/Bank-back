package ma.ensa.bank.agentHandler.agent;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/agent/")
public class AgentContoller {
    private final ClientService clientService;
    private final AgentService agentService;

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getClients(HttpServletRequest request){
        Agent agent = this.getCurrentAgent(request);
        return new ResponseEntity<>(agent.getClients(), HttpStatus.OK);
    }
    @GetMapping("/clientswithoutagent")
    public ResponseEntity<List<Client>> getClients_without_agent(){
        return new ResponseEntity(clientService.getClientsWithoutAgent(), HttpStatus.OK);
    }

    @PostMapping("/addclient")
    public ResponseEntity<?> addClient(@RequestBody ClientDTO client,HttpServletRequest request){
        if(client==null){
            return ResponseEntity.badRequest().body("This client is not valid");
        }
        client.setAgentId(this.getCurrentAgent(request).getIdCardNumber());
        Client newClient = clientService.addClient(client);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newClient);
    }

    @PostMapping("/assigneagent")
    public ResponseEntity<String> assign_client_to_agent(@RequestBody Assignids obj){
        if(obj==null){
            return ResponseEntity.badRequest().body("Data is not valid");
        }else if(clientService.getClientById(obj.getClientId()).isPresent() &&
                agentService.getAgentByEmail(obj.getAgentEmail())!=null){
            clientService.assign_client_to_agent(obj.getClientId(),obj.getAgentEmail());
            return new ResponseEntity<>(
                    "Assigned Succefully",
                    HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().body("Id doesn't exist");
        }
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
    public ResponseEntity<String> deleteagent(@PathVariable("clientid") Long clientid){
        if(clientid==null){
            return ResponseEntity.badRequest().body("The provided id is not valid");
        }
        if(clientService.getClientById(clientid).isPresent()){
            clientService.deleteClient(clientid);
            return new ResponseEntity<>("Well Deleted", HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body("Id doesn't exist!!");
        }
    }

    @PostMapping("toggleFav/{clientid}")
    public ResponseEntity<String> toggleFav(@PathVariable("clientid") Long id){
        System.out.println("clientid");
        if(id==null){
            return ResponseEntity.badRequest().body("This id is not valid");
        }else if(clientService.getClientById(id).isPresent()){
            clientService.toggleIsFavorite(id);
            return ResponseEntity.ok().body("Toggled Succesfully");
        }else{
            return ResponseEntity.badRequest().body("Id doesn't exist");
        }
    }
    @GetMapping("/getcurrentinfo")
    public Agent getCurrentAgent(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String jwt = token.substring(7);
        DecodedJWT jwtObject = JWT.decode(jwt);
        String Email = jwtObject.getSubject();
        Agent agent = agentService.getAgentByEmail(Email);
        return agent;
    }

}