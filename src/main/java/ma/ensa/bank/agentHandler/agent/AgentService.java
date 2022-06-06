package ma.ensa.bank.agentHandler.agent;

import lombok.AllArgsConstructor;
import ma.ensa.bank.backOfficeHandler.backOffice.ResponseBackOffice;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Agent signin(Agent agent) {
        boolean present = agentRepository.findByEmail(agent.getEmail()).isPresent();
        if (present) {
            Agent value = agentRepository.findByEmail(agent.getEmail()).get();
            if(bCryptPasswordEncoder.matches(agent.getPassword(), value.getPassword())) {
                ResponseBackOffice responseBackOffice = new ResponseBackOffice();
                BeanUtils.copyProperties(value, responseBackOffice);
                return value;
            }
            else {
                System.out.println(agent.getPassword()+"   "+agent.getEmail());
                throw new IllegalStateException("email or password invalid");
            }
        }
        else {
            throw new IllegalStateException("invalid request");
        }
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
    public void updateAgent(Long agentCardId,Agent agent){
        Agent dbagent = agentRepository.findAgentById(agentCardId).orElseThrow(
                ()-> new IllegalStateException("Agent don't exist!!")
        );
        if(agent.getFirstName()!=null && agent.getFirstName().length()>3 && !Objects.equals(dbagent.getFirstName(),agent.getFirstName())){
            dbagent.setFirstName(agent.getFirstName());
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
        if(agent.getDateOfBirth()!=null){
            dbagent.setDateOfBirth(agent.getDateOfBirth());
        }
    }
    public void deleteAgent(Long id){
        Optional<Agent> opt1 = agentRepository.findAgentById(id);
        if(opt1.isPresent()){
            agentRepository.deleteById(id);
        }else {
            throw new IllegalStateException("This Id doesn't exist!");
        }
    }

    public Agent getAgentByEmail(String email){
        return agentRepository.findByEmail(email).get();
    }


}
