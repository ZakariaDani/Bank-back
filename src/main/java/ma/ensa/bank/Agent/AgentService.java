package ma.ensa.bank.Agent;

import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgentService {
    private final AgentRepository agentRepository;

    @Autowired
    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> getAgents(){
        return agentRepository.findAll();
    }
    public void addAgent(Agent agent){
        Optional<Agent> opt1 = agentRepository.findAgentByEmail(agent.getEmail());
        Optional<Agent> opt2 = agentRepository.findAgentById(agent.getIdCardNumber());
        Optional<Agent> opt3 = agentRepository.findAgentByPhone(agent.getPhone());
        if(opt1.isPresent() || opt2.isPresent() || opt3.isPresent()){
            throw new IllegalStateException("Duplicate Data in the database!!");
        }else{
            System.out.println(agent);
            agent.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(agent.getPassword()));
            agentRepository.save(agent);
        }
    }
    @Transactional
    public void updateAgent(String agentCardId,Agent agent){
        Agent dbagent = agentRepository.findAgentById(agentCardId).orElseThrow(
                ()-> new IllegalStateException("Agent don't exist!!")
        );
        if(agent.getName()!=null && agent.getName().length()>3 && !Objects.equals(dbagent.getName(),agent.getName())){
            dbagent.setName(agent.getName());
        }
        if(agent.getEmail()!=null && agent.getEmail().length()>7 && !Objects.equals(dbagent.getEmail(),agent.getEmail())){
            Optional<Agent> opt = agentRepository.findAgentByEmail(agent.getEmail());
            if(opt.isPresent()){
                throw new IllegalStateException("email updated already exist!!");
            }
            dbagent.setEmail(agent.getEmail());
        }
        if(agent.getPhone()!=null && agent.getPhone().length()>9 && !Objects.equals(dbagent.getPhone(),agent.getPhone())){
            Optional<Agent> opt = agentRepository.findAgentByPhone(agent.getPhone());
            if(opt.isPresent()){
                throw new IllegalStateException("phone updated already exist!!");
            }
            dbagent.setPhone(agent.getPhone());
        }
        if(agent.getBirth()!=null){
            dbagent.setBirth(agent.getBirth());
        }
    }
    public void deleteAgent(String id){
        Optional<Agent> opt1 = agentRepository.findAgentByEmail(id);
        if(opt1.isPresent()){
            agentRepository.deleteById(id);
        }else {
            throw new IllegalStateException("This Id doesn't exist!");
        }
    }

    public Agent getAgentByEmail(String email){
        return agentRepository.findByEmail(email);
    }

}
