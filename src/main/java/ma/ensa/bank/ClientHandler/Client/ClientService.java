package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

    public void addClient(ClientDTO clientDTO){
        Optional<Client> opt1 = clientRepository.findClientByPhone(clientDTO.getPhone());
        Optional<Client> opt2 = clientRepository.findClientById(clientDTO.getId());
        Optional<Client> opt3 = clientRepository.findClientByEmail(clientDTO.getEmail());
        Pattern phonepatt = Pattern.compile("^\\d{10}$");
        Pattern emailpatt = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namespatt = Pattern.compile("^[A-Za-z]{3,7}");
        if(opt1.isPresent() || opt2.isPresent() || opt3.isPresent()){
            throw new IllegalStateException("Doublicate data in the database!!");
        }else if(clientDTO.getEmail().trim().isEmpty() || clientDTO.getPhone().trim().isEmpty() || clientDTO.getFname().trim().isEmpty() || clientDTO.getLname().trim().isEmpty() || clientDTO.getAddress().trim().isEmpty() || clientDTO.getBirth()==null){
            throw new IllegalStateException("All fields are required!");
        }else{
            if(!phonepatt.matcher(clientDTO.getPhone()).matches()){
                throw new IllegalStateException("Phone can containe only digits!!");
            }
            if(!emailpatt.matcher(clientDTO.getEmail()).matches()){
                throw new IllegalStateException("Email format is not valid!!");
            }
            if(!namespatt.matcher(clientDTO.getFname()).matches()){
                throw new IllegalStateException("First name is not valid");
            }
            if(!namespatt.matcher(clientDTO.getLname()).matches()){
                throw new IllegalStateException("Last name is not valid");
            }
            clientDTO.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(clientDTO.getPassword()));
            Client client = new Client();
            client.setId(clientDTO.getId());
            client.setFname(clientDTO.getFname());
            client.setLname(clientDTO.getLname());
            client.setPhone(clientDTO.getPhone());
            client.setBirth(clientDTO.getBirth());
            client.setEmail(clientDTO.getEmail());
            client.setPassword(clientDTO.getPassword());
            client.setSolde(clientDTO.getSolde());
            client.setAddress(clientDTO.getAddress());
            clientRepository.save(client);
        }
    }

    @Transactional
    public void updateClient(Long ClientId,Client client){
        Pattern phonepatt = Pattern.compile("^\\d{10}$");
        Pattern emailpatt = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namespatt = Pattern.compile("^[A-Za-z]{3,10}$");
        Client clientdb = clientRepository.findClientById(ClientId).orElseThrow(
                ()-> new IllegalStateException("Client by this id doesn't exist!!")
        );
        if(!phonepatt.matcher(client.getPhone()).matches()){
            throw new IllegalStateException("Phone can containe only digits!!");
        }
        if(!emailpatt.matcher(client.getEmail()).matches()){
            throw new IllegalStateException("Email format is not valid!!");
        }
        if(!namespatt.matcher(client.getFname()).matches()){
            throw new IllegalStateException("First name is not valid");
        }
        if(!namespatt.matcher(client.getLname()).matches()){
            throw new IllegalStateException("Last name is not valid");
        }
        if(client.getFname()!=null && client.getFname().length()>3 && !Objects.equals(clientdb.getFname(),client.getFname())){
            clientdb.setFname(client.getFname());
        }
        if(client.getLname()!=null && client.getLname().length()>3 && !Objects.equals(clientdb.getLname(),client.getLname())){
            clientdb.setLname(client.getLname());
        }
        if(client.getEmail()!=null && client.getEmail().length()>7 && !Objects.equals(clientdb.getEmail(),client.getEmail())){
            Optional<Client> opt = clientRepository.findClientByEmail(client.getEmail());
            if(opt.isPresent()){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientdb.setEmail(client.getEmail());
        }
        if(client.getAddress()!=null && client.getAddress().length()>5 && !Objects.equals(clientdb.getAddress(),client.getAddress())){
            clientdb.setEmail(client.getEmail());
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
        System.out.println("mami");

    }
    public void deleteAgent(Long id){
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

    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone);
    }
}




