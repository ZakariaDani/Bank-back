package ma.ensa.bank.agentHandler.agent;

import lombok.AllArgsConstructor;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.backOfficeHandler.backOffice.ResponseBackOffice;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import ma.ensa.bank.email.EmailEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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
            agent.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(agent.getPassword()));
            agentRepository.save(agent);
        }
    }
    public Agent updateAgent(Agent existedAgent, AgentDTO agentDTO){
        String firstName = agentDTO.getFirstName();
        if(firstName != null) {
            existedAgent.setFirstName(firstName);
        }
        String lastName = agentDTO.getLastName();
        if(lastName != null) {
            existedAgent.setLastName(lastName);
        }
        String phone = agentDTO.getPhone();
        if(phone != null) {
            existedAgent.setPhone(phone);
        }
        String email = agentDTO.getEmail();
        if(email != null) {
            existedAgent.setEmail(email);
        }
        String description = agentDTO.getDescription();
        if(description != null) {
            existedAgent.setDescription(description);
        }
        String patente = agentDTO.getPatente();
        if(patente != null) {
            existedAgent.setPatente(patente);
        }
        String adress = agentDTO.getAdress();
        if(adress != null) {
            existedAgent.setAdress(adress);
        }
        String matricule = agentDTO.getMatricule();
        if(matricule != null) {
            existedAgent.setMatricule(matricule);
        }

        LocalDate dateOfBirth = agentDTO.getDateOfBirth();
        if(dateOfBirth != null) {
            existedAgent.setDateOfBirth(dateOfBirth);
        }
        return agentRepository.save(existedAgent);
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
        if(agentRepository.findByEmail(email).isPresent()){
            return agentRepository.findByEmail(email).get();
        }
        return null;
    }
    public Optional<Agent> getAgentById(Long id){
        return agentRepository.findAgentById(id);
    }
}