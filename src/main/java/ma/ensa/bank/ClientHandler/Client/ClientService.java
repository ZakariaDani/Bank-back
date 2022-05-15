package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
        boolean present = clientRepository.findClientByPhone(client.getPhone()) != null;
        if (present) {
            Client value = clientRepository.findClientByEmail(client.getEmail());
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
        Client opt1 = clientRepository.findClientByPhone(client.getPhone());
        Client opt2 = clientRepository.findClientByEmail(client.getEmail());
        if(opt1!=null || opt2 != null ){
            throw new IllegalStateException("Client already exist!!");
        }else{
            System.out.println(client);
            client.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(client.getPassword()));
            clientRepository.save(client);
        }
    }

    @PostMapping("/update")
    public void updateClient(String email , Client client){

        Client clientdb = clientRepository.findClientByEmail(email);

        if(client.getFname()!=null && client.getFname().length()>3 && !Objects.equals(clientdb.getFname(),client.getFname())){
            clientdb.setFname(client.getFname());
        }
        if(client.getLname()!=null && client.getLname().length()>3 && !Objects.equals(clientdb.getLname(),client.getLname())){
            clientdb.setLname(client.getLname());
        }
        if(client.getEmail()!=null && client.getEmail().length()>7 && !Objects.equals(clientdb.getEmail(),client.getEmail())){
            Client opt = clientRepository.findClientByEmail(client.getEmail());
            if(opt != null){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientdb.setEmail(client.getEmail());
        }
        if(client.getPhone()!=null && client.getPhone().length()>9 && !Objects.equals(clientdb.getPhone(),client.getPhone())){
            Client opt = clientRepository.findClientByPhone(client.getPhone());
            if(opt != null ){
                throw new IllegalStateException("phone you want to update already exist!!");
            }
            clientdb.setPhone(client.getPhone());
        }

    }

    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone);
    }
}




