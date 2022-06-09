package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentRepository;
import ma.ensa.bank.agentHandler.agent.AgentService;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import ma.ensa.bank.email.EmailEntity;
import ma.ensa.bank.email.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final AgentRepository agentRepository;
    private final EmailService emailService;

    @Autowired
    public ClientService(ClientRepository clientRepository,AgentRepository agentRepository, EmailService emailService) {
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
        this.emailService = emailService;
    }

    public ClientDTO SignIn(Client client) {
        boolean present = clientRepository.findClientByPhone(client.getPhone()).isPresent();
        if (present) {
            Client value = clientRepository.findClientByEmail(client.getEmail()).get();
            if(client.getPassword().equals(value.getPassword())) {
                ClientDTO response = new ClientDTO();
                BeanUtils.copyProperties(value, response);
                clientRepository.save(value);
                return response;
            }
            else {
                throw new IllegalStateException("Phone or Password invalid");
            }
        }
        else {
            throw new IllegalStateException("invalid request");
        }
    }

    public Client addClient(ClientDTO clientDTO){
        Optional<Client> opt1 = clientRepository.findClientByPhone(clientDTO.getPhone());
        Optional<Client> opt2 = clientRepository.findClientById(clientDTO.getId());
        Optional<Client> opt3 = clientRepository.findClientByEmail(clientDTO.getEmail());
        Pattern phonepatt = Pattern.compile("^\\d{10}$");
        Pattern emailpatt = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namespatt = Pattern.compile("^[A-Za-z]{3,20}");
        if(opt1.isPresent() || opt2.isPresent() || opt3.isPresent()){
            throw new IllegalStateException("Doublicate data in the database!!");
        }else if(clientDTO.getEmail().trim().isEmpty() || clientDTO.getPhone().trim().isEmpty() || clientDTO.getFirstName().trim().isEmpty() || clientDTO.getLastName().trim().isEmpty() || clientDTO.getAddress().trim().isEmpty() || clientDTO.getBirth()==null){
            throw new IllegalStateException("All fields are required!");
        }else{
            if(!phonepatt.matcher(clientDTO.getPhone()).matches()){
                throw new IllegalStateException("Phone number is not valid!!");
            }
            if(!emailpatt.matcher(clientDTO.getEmail()).matches()){
                throw new IllegalStateException("Email format is not valid!!");
            }
            if(!namespatt.matcher(clientDTO.getFirstName()).matches()){
                throw new IllegalStateException("First name is not valid");
            }
            if(!namespatt.matcher(clientDTO.getLastName()).matches()){
                throw new IllegalStateException("Last name is not valid");
            }
            clientDTO.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(clientDTO.getPassword()));
            Client client = new Client();
            client.setId(clientDTO.getId());
            client.setFirstName(clientDTO.getFirstName());
            client.setLastName(clientDTO.getLastName());
            client.setPhone(clientDTO.getPhone());
            client.setBirth(clientDTO.getBirth());
            client.setEmail(clientDTO.getEmail());
            client.setPassword(clientDTO.getPassword());
            client.setSolde(clientDTO.getSolde());
            client.setAddress(clientDTO.getAddress());
            client.setIsFavorite((clientDTO.getIsFavorite())?false:client.getIsFavorite());
            if(clientDTO.getAgentId()!=null) {
                Agent agent = agentRepository.findAgentById(clientDTO.getAgentId()).get();
                client.setAgent(agent);
            }
            clientRepository.save(client);
            return client;
        }
    }

    @Transactional
    public Client updateClient(Long ClientId,Client client){
        Pattern phonepatt = Pattern.compile("^\\d{10}$");
        Pattern emailpatt = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namespatt = Pattern.compile("^[A-Za-z]{3,20}$");
        Client clientdb = clientRepository.findClientById(ClientId).get();
        if(!phonepatt.matcher(client.getPhone()).matches()){
            throw new IllegalStateException("Phone number is not valid");
        }
        if(!emailpatt.matcher(client.getEmail()).matches()){
            throw new IllegalStateException("Email format is not valid!!");
        }
        if(!namespatt.matcher(client.getFirstName()).matches()){
            throw new IllegalStateException("First name is not valid");
        }
        if(!namespatt.matcher(client.getLastName()).matches()){
            throw new IllegalStateException("Last name is not valid");
        }
        if(client.getFirstName()!=null && client.getFirstName().length()>3 && !Objects.equals(clientdb.getFirstName(),client.getFirstName())){
            clientdb.setFirstName(client.getFirstName());
        }
        if(client.getLastName()!=null && client.getLastName().length()>3 && !Objects.equals(clientdb.getLastName(),client.getLastName())){
            clientdb.setLastName(client.getLastName());
        }
        if(client.getEmail()!=null && client.getEmail().length()>7 && !Objects.equals(clientdb.getEmail(),client.getEmail())){
            Optional<Client> opt = clientRepository.findClientByEmail(client.getEmail());
            if(opt.isPresent()){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientdb.setEmail(client.getEmail());
        }
        if(client.getAddress()!=null && client.getAddress().length()>5 && !Objects.equals(clientdb.getAddress(),client.getAddress())){
            clientdb.setAddress(client.getAddress());
        }
        if(client.getPhone()!=null && client.getPhone().length()>9 && !Objects.equals(clientdb.getPhone(),client.getPhone())){
            Optional<Client> opt = clientRepository.findClientByPhone(client.getPhone());
            if(opt.isPresent()){
                throw new IllegalStateException("phone you want to update already exist!!");
            }
            clientdb.setPhone(client.getPhone());
        }
        if(client.getBirth()!=null){
            clientdb.setBirth(client.getBirth());
        }
        if(client.getIsFavorite()!=null){
            clientdb.setIsFavorite(client.getIsFavorite());
        }
        if(client.getSolde()!=null){
            clientdb.setSolde(client.getSolde());
        }
        return clientdb;
    }
    @Transactional
    public void toggleIsFavorite(Long id){
        Client clientDb = clientRepository.findClientById(id).get();
        clientDb.setIsFavorite(!clientDb.getIsFavorite());
    }
    @Transactional
    public void assign_client_to_agent(Long idClient,String emailAgent){
        Client clientDb = clientRepository.findClientById(idClient).get();
        Agent agent = agentRepository.findAgentByEmail(emailAgent).get();
        clientDb.setAgent(agent);
    }
    public void deleteClient(Long id){
        Optional<Client> opt1 = clientRepository.findClientById(id);
        if(opt1.isPresent()){
            clientRepository.deleteById(id);
        }else {
            throw new IllegalStateException("This Id doesn't exist!");
        }
    }
    public List<Client> getClients(){
        return clientRepository.findAll();
    }
    public Optional<List<Client>> getClientsWithoutAgent(){
        return clientRepository.findClientWithoutAgent();
    }
    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone);
    }

    public Optional<Client> getClientById(Long Id){ return clientRepository.findClientById(Id);};

    public String sendCodeToClient(Long id){
        Optional<Client> c = this.getClientById(id);
        if(c.isPresent()){
            Client client  = c.get();
            String password = new Random().ints(10, 33, 122).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            client.setPassword(password);
            this.updateClient(id, client);
            String message = "Hello Mr" + client.getFirstName() + client.getLastName() + "Your New Password is: " + client.getPassword();
            EmailEntity emailEntity = new EmailEntity(client.getEmail(), "your New Password", message);
            try {
                emailService.sendMail(emailEntity);
                return "Succes";
            }catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
        }
        return "Error";
    }
    public List<Client> getClientsByAgentId(Long agentId){
        return clientRepository.getClientsByAgentId(agentId).get();
    }
}




