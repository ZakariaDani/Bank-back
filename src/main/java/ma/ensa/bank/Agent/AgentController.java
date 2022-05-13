package ma.ensa.bank.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AgentController {
    private final AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }
    @CrossOrigin
    @GetMapping("w")
    public List<Agent> getAgents(){
        return agentService.getAgents();
    }
    @CrossOrigin
    @PostMapping("/addagent")
    public void addAgent(@RequestBody Agent agent){
        if(agent==null){
            throw new IllegalStateException("All Information Are Required");
        }else{
            agentService.addAgent(agent);
        }
    }
    @CrossOrigin
    @DeleteMapping(value="/deleteagent/{agentCardId}")
    public void deleteagent(@PathVariable("agentCardId") Long agentCardId){
        if(agentCardId==null){
            throw new IllegalStateException("Please Enter a valid CardId");
        }else {
            agentService.deleteAgent(agentCardId);
        }
    }
    @CrossOrigin
    @PutMapping(value="/updateagent/{agentCardId}")
    public void updateagent(@PathVariable("agentCardId") Long agentCardId,@RequestBody Agent agent){
        agentService.updateAgent(agentCardId,agent);
    }


}
