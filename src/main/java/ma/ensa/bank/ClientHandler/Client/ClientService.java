package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;
import  ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransactionService;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.Transaction;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.TransactionService;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCodeService;
import ma.ensa.bank.Helpers.CurrentUserInfo;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentRepository;
import ma.ensa.bank.agentHandler.agent.AgentService;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import ma.ensa.bank.email.EmailEntity;
import ma.ensa.bank.email.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final AgentRepository agentRepository;
    private final EmailService emailService;

    @Autowired
    private NotValidatedTransactionService notValidatedTransactionService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private VerificationCodeService verificationCodeService;

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
            boolean opt = clientRepository.findClientByEmail(client.getEmail()).isPresent();
            if(opt){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientdb.setEmail(client.getEmail());
        }
        if(client.getAddress()!=null && client.getAddress().length()>5 && !Objects.equals(clientdb.getAddress(),client.getAddress())){
            clientdb.setAddress(client.getAddress());
        }
        if(client.getPhone()!=null && client.getPhone().length()>9 && !Objects.equals(clientdb.getPhone(),client.getPhone())){
            boolean opt = clientRepository.findClientByPhone(client.getPhone()).isPresent();
            if(opt){
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
        this.sendCodeToClient(clientDb.getId());
        clientDb.setAgent(agent);
    }
    public String sendCodeToClient(Long id){
        Optional<Client> c = this.getClientById(id);
        if(c.isPresent()){
            Client client  = c.get();
            String password = new Random().ints(10, 33, 122).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            String EncodePass = PasswordEncoder.bCryptPasswordEncoder().encode(password);
            client.setPassword(EncodePass);
            this.updateClient(id, client);
            String message = "Hello Mr" + client.getFirstName() + client.getLastName() + "Your New Password is: " + password;
            EmailEntity emailEntity = new EmailEntity(client.getEmail(), "your New Password", message);

            try {
                emailService.sendMail(emailEntity);
                System.out.println("email is sending to: "+client.getEmail()+"\nmessage is: "+ emailEntity);
                return "Succes";
            }catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
        }
        return "Error";
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
        return clientRepository.findClientByPhone(phone).get();
    }

    public Optional<Client> getClientById(Long Id){ return clientRepository.findClientById(Id);};


    public List<Client> getClientsByAgentId(Long agentId){
        return clientRepository.getClientsByAgentId(agentId).get();
    }

    @Transactional
    public Long makeTransaction(String emitterPhone, String receiverPhone, double amount){

        Pattern p = Pattern.compile("^0[5-7][0-9]+");//. represents single character
        Matcher m = p.matcher(receiverPhone);
        boolean validPhoneNumber = m.matches();

        if( validPhoneNumber == true && amount>0 ){
            try{
                Client emitter  = clientRepository.findClientByPhone(emitterPhone).get();
                Client receiver  = clientRepository.findClientByPhone(receiverPhone).get();
                if(receiver == null){
                    throw new RuntimeException("There is no Client with that phone number");
                }
                else if(emitter.getSolde() < amount){
                    throw new RuntimeException("You don't have enough money to do this transaction");
                }
                else{
                    NotValidatedTransaction transaction = notValidatedTransactionService.
                            saveTransaction(emitterPhone,receiverPhone,amount);

                    verificationCodeService.sendVerificationCode(emitterPhone, amount, transaction);

                    return transaction.getId();
                }
            }
            catch(Exception exception){
                throw exception;
            }
        }

        else{
            throw new RuntimeException("The phone number must start with 0{5,6,7}" +
                    " and the amount must be a positive number");
        }
    }

    @Transactional
    public Long makeTelecomRecharge(String emitterPhone, String telecomEntreprise, double amount){

        List<String> telecomEntreprises = Arrays.asList("orange","maroc_telecom","inwi");
        List<String> rechargeTypes = Arrays.asList("5","10","20","50","100","200");

        if(telecomEntreprises.contains(telecomEntreprise) == false){
            throw new RuntimeException("We provide only Orange,Maroc Telecom,Inwi for telecom recharging");
        }
        else if(rechargeTypes.contains(""+(int)amount) == false){
            throw new RuntimeException("The amount must be a in {5,10,20,50,100,200}");
        }
        else{
            Client emitter = clientRepository.findClientByPhone(emitterPhone).get();
            if(emitter.getSolde()< amount){
                throw new RuntimeException("You don't have enough money to do this transaction");
            }
            else{
                NotValidatedTransaction transaction = notValidatedTransactionService.
                        saveTransaction(emitterPhone,telecomEntreprise,amount);

                verificationCodeService.sendVerificationCode(emitterPhone, amount, transaction);

                return transaction.getId();
            }

        }

    }

    @Transactional
    public void receive_verification_code(Long transactionID,String code,String phoneNumber){

        VerificationCode verificationCodeDB = verificationCodeService.getVerificationCode(transactionID,phoneNumber);

       if(verificationCodeDB != null) {
           if (verificationCodeDB.getCode().equals(code) == false) {
               throw new RuntimeException("Incorrect verification code");
           } else {
               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
               LocalDateTime now = LocalDateTime.now();
               LocalDateTime verificationDate = LocalDateTime.parse(verificationCodeDB.getDate(), dtf);
               long time_diff = ChronoUnit.SECONDS.between(verificationDate, now);

            /* We check if this verification code has expired by
            comparing the time difference with 300second = 5min
             */
               if (time_diff > 300) {
                   throw new RuntimeException("This verification code has been expired");
               } else {
                   NotValidatedTransaction notValidatedTransaction = verificationCodeDB.getTransaction();

                   if (notValidatedTransaction != null) {
                       Client emitter = clientRepository.findClientByPhone(notValidatedTransaction.getEmitter()).get();
                       double amount = notValidatedTransaction.getAmount();

                       if (emitter.getSolde() > amount) {
                           String receiverPhone = notValidatedTransaction.getReceiver();
                           boolean receiver_is_a_client;
                           if(receiverPhone.charAt(0) == '0'){
                               receiver_is_a_client = true;
                           }
                           else{
                               receiver_is_a_client = false;
                           }

                           transactionService.saveTransaction(
                                   notValidatedTransaction.getEmitter(),
                                   notValidatedTransaction.getReceiver(),
                                   notValidatedTransaction.getAmount()
                           );

                           notValidatedTransactionService.deleteTransaction(notValidatedTransaction.getId());

                           emitter.setSolde(emitter.getSolde() - amount);
                           if(receiver_is_a_client == true){
                              Client receiver = clientRepository.findClientByPhone(receiverPhone).get();
                              receiver.setSolde(receiver.getSolde() + amount);
                           }

                       } else {
                           throw new RuntimeException("You don't have enough money to do that transaction");
                       }
                   } else {
                       throw new RuntimeException("This transaction is already done");
                   }
               }
           }
       }

       else{
           throw new RuntimeException("There is no transaction with that id");
       }

    }

    public List<Transaction> getTransactions(String currentUserPhoneNumber){
        return transactionService.getTransactions(currentUserPhoneNumber);
    }
}

