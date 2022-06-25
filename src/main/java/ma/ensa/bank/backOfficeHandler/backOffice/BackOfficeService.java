package ma.ensa.bank.backOfficeHandler.backOffice;


import lombok.AllArgsConstructor;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentDTO;
import ma.ensa.bank.agentHandler.agent.AgentRepository;
import ma.ensa.bank.image.ImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BackOfficeService {

    private final BackOfficeRepository backOfficeRepository;
    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageService imageService;

    public BackOffice updateBackOffice(BackOffice existedBackOffice, BackOfficeDTO backOfficeDTO){
        String firstName = backOfficeDTO.getFirstName();
        if(firstName != null) {
            existedBackOffice.setFirstName(firstName);
        }
        String lastName = backOfficeDTO.getLastName();
        if(lastName != null) {
            existedBackOffice.setLastName(lastName);
        }
        String phone = backOfficeDTO.getPhone();
        if(phone != null) {
            existedBackOffice.setPhone(phone);
        }
        String email = backOfficeDTO.getEmail();
        if(email != null) {
            existedBackOffice.setEmail(email);
        }


        LocalDate dateOfBirth = backOfficeDTO.getDateOfBirth();
        if(dateOfBirth != null) {
            existedBackOffice.setDateOfBirth(dateOfBirth);
        }
        return backOfficeRepository.save(existedBackOffice);
    }

    public ResponseBackOffice signin(BackOffice backOffice) {
        boolean present = backOfficeRepository.findByEmail(backOffice.getEmail()).isPresent();
        if (present) {
            BackOffice value = backOfficeRepository.findByEmail(backOffice.getEmail()).get();
            if(bCryptPasswordEncoder.matches(backOffice.getPassword(), value.getPassword())) {
                ResponseBackOffice responseBackOffice = new ResponseBackOffice();
                BeanUtils.copyProperties(value, responseBackOffice);
                backOfficeRepository.save(value);
                return responseBackOffice;
            }
            else {
                System.out.println(backOffice.getPassword()+"   "+backOffice.getEmail());
                throw new IllegalStateException("email or password invalid");
            }
        }
        else {
            throw new IllegalStateException("invalid request");
        }
    }
    public BackOffice getBackOfficeByEmail(String email){
        if(backOfficeRepository.findByEmail(email).isPresent()){
            return backOfficeRepository.findByEmail(email).get();
        }
        return null;
    }
    public Agent getAgentByEmail(String email){
        return agentRepository.findByEmail(email).get();
    }
    public List<Agent> getAllAgents(){
        return agentRepository.findAll();
    }

    public Optional<Agent> getAgentById(Long id){
        return agentRepository.findAgentById(id);
    }

    public Agent saveAgent(AgentDTO agentDTO){
        Agent agent = new Agent();
        agent.setFirstName(agentDTO.getFirstName());
        agent.setLastName(agentDTO.getLastName());
        agent.setPhone(agentDTO.getPhone());
        agent.setEmail(agentDTO.getEmail());
        agent.setDateOfBirth(agentDTO.getDateOfBirth());
        agent.setPatente(agentDTO.getPatente());
        agent.setMatricule(agentDTO.getMatricule());
        agent.setAdress(agentDTO.getAdress());
        agent.setDescription(agentDTO.getDescription());
        agent.setPassword(bCryptPasswordEncoder.encode(agentDTO.getPassword()));
        BackOffice backOffice = backOfficeRepository.findByEmail(agentDTO.getBackofficeEmail()).get();
        agent.setBackOffice(backOffice);

        return agentRepository.save(agent);
    }

    public void saveAgentImage(Long id, String fileName) {
	Optional<Agent> existedAgent = getAgentById(id);
        if(existedAgent.isPresent()) {
            Agent currentAgent = existedAgent.get();
	    currentAgent.setFileName(fileName);
	    agentRepository.save(currentAgent);
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

    public Agent updateFavoriteAgent(Agent existedAgent, AgentDTO agentDTO){
        boolean isFavorite = agentDTO.isFavorite();
        existedAgent.setFavorite(isFavorite);
        return agentRepository.save(existedAgent);
    }

    @Transactional
    public void deleteAgent(String agentEmail){
        agentRepository.deleteByEmail(agentEmail);
    }

    public List<Agent> getFavoriteAgents(){return agentRepository.findFavoriteAgents();}

}