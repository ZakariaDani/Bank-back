package ma.ensa.bank.backOfficeHandler.backOffice;


import lombok.AllArgsConstructor;
import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.Agent.AgentDTO;
import ma.ensa.bank.Agent.AgentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BackOfficeService {

    private final BackOfficeRepository backOfficeRepository;
    private final AgentRepository agentRepository;

    public ResponseBackOffice signin(BackOffice backOffice) {
        boolean present = backOfficeRepository.findByEmail(backOffice.getEmail()).isPresent();
        if (present) {
            BackOffice value = backOfficeRepository.findByEmail(backOffice.getEmail()).get();
            if(backOffice.getPassword().equals(value.getPassword())) {
                ResponseBackOffice responseBackOffice = new ResponseBackOffice();
                BeanUtils.copyProperties(value, responseBackOffice);
                backOfficeRepository.save(value);
                return responseBackOffice;
            }
            else {
                throw new IllegalStateException("email or password invalid");
            }
        }
        else {
            throw new IllegalStateException("invalid request");
        }
    }
    public BackOffice getBackOfficeByEmail(String email){
        return backOfficeRepository.findByEmail(email).get();
    }
    public List<Agent> getAllAgents(){
        return agentRepository.findAll();
    }

    public Optional<Agent> getAgentById(String id){
       return agentRepository.findAgentById(id);
    }

    public Agent saveAgent(AgentDTO agentDTO){
        Agent agent = new Agent();
        agent.setName(agentDTO.getName());
        agent.setPhone(agentDTO.getPhone());
        agent.setEmail(agentDTO.getEmail());
        agent.setBirth(agentDTO.getBirth());
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent existedAgent, AgentDTO agentDTO){
        String name = agentDTO.getName();
        if(name != null) {
            existedAgent.setName(name);;
        }
        String phone = agentDTO.getPhone();
        if(phone != null) {
            existedAgent.setPhone(phone);;
        }
        String email = agentDTO.getEmail();
        if(email != null) {
            existedAgent.setEmail(email);;
        }
        LocalDate birth = agentDTO.getBirth();
        if(birth != null) {
            existedAgent.setBirth(birth);
        }
        return agentRepository.save(existedAgent);
    }

    public void deleteAgent(String id){
        agentRepository.deleteById(id);
    }
}