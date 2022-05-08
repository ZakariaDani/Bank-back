package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
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

    public void addClient(Client client){
        Optional<Client> opt1 = clientRepository.findClientByPhone(client.getPhone());
        Optional<Client> opt2 = clientRepository.findClientById(client.getId());
        if(opt1.isPresent() || opt2.isPresent() ){
            throw new IllegalStateException("Client already exist!!");
        }else{
            System.out.println(client);
            client.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(client.getPassword()));
            clientRepository.save(client);
        }
    }

    @Transactional
    public void updateClient(String ClientCardId,Client client){
        Client clientdb = clientRepository.findClientById(ClientCardId).orElseThrow(
                ()-> new IllegalStateException("Client doesn't exist!!")
        );
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
        if(client.getPhone()!=null && client.getPhone().length()>9 && !Objects.equals(clientdb.getPhone(),client.getPhone())){
            Optional<Client> opt = clientRepository.findClientByPhone(client.getPhone());
            if(opt.isPresent()){
                throw new IllegalStateException("phone you want to update already exist!!");
            }
            clientdb.setPhone(client.getPhone());
        }

    }

    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone);
    }
}




