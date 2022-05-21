package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.AllArgsConstructor;
import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.Agent.AgentDTO;
import ma.ensa.bank.Agent.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/backoffice/agents")
public class BackOfficeController {
    private final BackOfficeService backOfficeService;
    private final AgentService agentService;

    @GetMapping
    public ResponseEntity<List<Agent>> getAgents() {
        return new ResponseEntity<>(backOfficeService.getAllAgents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAgent(@PathVariable("id") final Long id) {
        Optional<Agent> agent = backOfficeService.getAgentById(id);
        if(agent.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(agent.get());
        } else {
            return ResponseEntity.badRequest().body("There is no agent with that specific id");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAgent(@RequestBody AgentDTO agentDTO) {
        if (agentDTO == null)
            return ResponseEntity.badRequest().body("The provided agent is not valid");
        Agent newAgent = backOfficeService.saveAgent(agentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newAgent);
    }


    @PatchMapping ("/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable("id") final Long id, @RequestBody AgentDTO agentDTO) {
        System.out.println("here");
        if (agentDTO == null)
            return ResponseEntity.badRequest().body("The provided agent is not valid");

        Optional<Agent> existedAgent = backOfficeService.getAgentById(id);
        if(existedAgent.isPresent()) {
            Agent currentAgent = existedAgent.get();

            Agent newAgent = backOfficeService.updateAgent(currentAgent, agentDTO);
            return ResponseEntity
                    .ok()
                    .body(newAgent);
        } else {
            return ResponseEntity.badRequest().body("There is no agent with that specific id");
        }
    }


    @DeleteMapping("/{agentEmail}")
    public ResponseEntity<?> deleteAgent(@PathVariable("agentEmail") String agentEmail) {
        if (agentEmail == null)
            return ResponseEntity.badRequest().body("The given agent is not valid");
        if (agentService.getAgentByEmail(agentEmail) == null)
            return ResponseEntity.badRequest().body("The given agent is not valid");
        backOfficeService.deleteAgent(agentEmail);
        return ResponseEntity.ok().body(agentService.getAgentByEmail(agentEmail));
    }
}
