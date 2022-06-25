package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;
import  ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransactionService;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.TransactionService;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCodeService;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentRepository;

import ma.ensa.bank.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientService {

    Logger log = LoggerFactory.getLogger(ClientController.class);
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

    public void addClient(Client client) {

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namesPattern = Pattern.compile("^[A-Za-z]{3,20}");

        boolean there_is_a_null_attribute = client.getPhone() == null || client.getLname() == null ||
                client.getFname() == null || client.getEmail() == null || client.getPlafon() == 0;

        if (there_is_a_null_attribute) {
            throw new IllegalStateException("All the fields should be filled in");
        } else {
             boolean opt1 = clientRepository.findClientByPhone(client.getPhone()).isPresent();
             boolean opt2 = clientRepository.findClientByEmail(client.getEmail()).isPresent();
            if(!emailPattern.matcher(client.getEmail()).matches()){
                throw new IllegalStateException("Invalid email addresse");
            }
            else if (!namesPattern.matcher(client.getFname()).matches()){
                throw new IllegalStateException("Invalid first name address");
            }
            else if (!namesPattern.matcher(client.getLname()).matches()){
                throw new IllegalStateException("Invalid last name  address");
            }
            else if (opt1 == true) {
                throw new IllegalStateException("This phone number is already used");
            } else if (opt2 == true) {
                throw new IllegalStateException("This email is already used");
            } else {

                client.setSolde(0);
                client.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode("123456"));
                client.setBirth(LocalDate.now());

                clientRepository.save(client);
            }
        }
    }

    @Transactional
    public Client updateClient(Long ClientId,Client client){

        Pattern phonePattern = Pattern.compile("^\\d{10}$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namesPattern = Pattern.compile("^[A-Za-z]{3,20}$");
        Client clientDB = clientRepository.findClientById(ClientId).get();
        if(!phonePattern.matcher(client.getPhone()).matches()){
            throw new IllegalStateException("Phone number is not valid");
        }
        if(!emailPattern.matcher(client.getEmail()).matches()){
            throw new IllegalStateException("Email format is not valid!!");
        }
        if(!namesPattern.matcher(client.getFname()).matches()){
            throw new IllegalStateException("First name is not valid");

        }
        if(!namesPattern.matcher(client.getLname()).matches()){
            throw new IllegalStateException("Last name is not valid");
        }
        if(client.getFname()!=null && client.getLname().length()>3 &&
                !Objects.equals(clientDB.getFname(),client.getFname())){

            clientDB.setFname(client.getFname());
        }
        if(client.getLname()!=null && client.getLname().length()>3 &&
                !Objects.equals(clientDB.getLname(),client.getLname())){
            clientDB.setLname(client.getLname());
        }
        if(client.getEmail()!=null && client.getEmail().length()>7 &&
                !Objects.equals(clientDB.getEmail(),client.getEmail())){
            boolean opt = clientRepository.findClientByEmail(client.getEmail()).isPresent();
            if(opt == true){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientDB.setEmail(client.getEmail());
        }
        if(client.getAddress()!=null && client.getAddress().length()>5 &&
                !Objects.equals(clientDB.getAddress(),client.getAddress())){
            clientDB.setAddress(client.getAddress());
        }
        if(client.getPhone()!=null && client.getPhone().length()>9 &&
                !Objects.equals(clientDB.getPhone(),client.getPhone())){
            boolean opt = clientRepository.findClientByPhone(client.getPhone()).isPresent();
            if(opt){
                throw new IllegalStateException("phone you want to update already exist!!");
            }
            clientDB.setPhone(client.getPhone());
        }
        if(client.getBirth()!=null){
            clientDB.setBirth(client.getBirth());
        }
        if(client.getIsFavorite()!=null){
            clientDB.setIsFavorite(client.getIsFavorite());
        }
        if(client.getSolde()!=0){
            clientDB.setSolde(client.getSolde());
        }
        return clientDB;
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

        if(validPhoneNumber == false){
            throw new RuntimeException("The phone number must start with 05 | 06 | 07");
        }
        else if( amount <=0){
            throw new RuntimeException("The amount must be a positive number different from zero");
        }
        else if(emitterPhone.equals(receiverPhone)){
            throw new RuntimeException("You can't send money to your self");
        }
        else{
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

    }

    /* this function is called when the user wants to make a telecomRecharge
        So in order to confirm that service we need firstly verify if:
            + we provide this telecom entreprise
            + we provide the amount requested
            + the user has enough money to make that transaction
     */
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

                System.out.println("Sendig the SMS to the user ....");
                verificationCodeService.sendVerificationCode(emitterPhone, amount, transaction);

                return transaction.getId();
            }

        }

    }

    /* this function is called when the user sends a verification code in
        order to confirm his transaction */
    /*So in this function we need to verify:
            + The transaction actually exists and still not confirmed
            + The verification code sent by the user is correct
            + The verification code still not expired;
            + Verify for the second time that :
                + The client has enough money
                + The receiver actually exists
     */
    @Transactional
    public void receive_verification_code(Long transactionID,String code,String phoneNumber){

        VerificationCode verificationCodeDB = verificationCodeService.getVerificationCode(transactionID,phoneNumber);

       if(verificationCodeDB != null) {
           String decrypted_code = PasswordEncoder.myDecreptionAlgorithm(
                   verificationCodeDB.getCode());

           if (decrypted_code.equals(code) == false) {
               throw new RuntimeException("Incorrect verification code");
           }
           else {

               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
               LocalDateTime now = LocalDateTime.now();
               LocalDateTime verificationDate = LocalDateTime.parse(verificationCodeDB.getDate(), dtf);
               long time_diff = ChronoUnit.SECONDS.between(verificationDate, now);

            /*
            We check if this verification code has expired by
            comparing the time difference with 300second = 5min
             */
               if (time_diff > 300) {
                   throw new RuntimeException("This verification code has been expired");
               }
               else {
                   NotValidatedTransaction notValidatedTransaction = verificationCodeDB.getTransaction();

                   if (notValidatedTransaction != null) {
                       Client emitter = clientRepository.findClientByPhone(notValidatedTransaction.getEmitter()).get();
                       double amount = notValidatedTransaction.getAmount();

                       if (emitter.getSolde() > amount) {
                           String receiverPhone = notValidatedTransaction.getReceiver();

                           //this variable will help us to distinguish if that transaction
                           //is a telecom recharge or transaction for a client
                           boolean receiver_is_a_client = receiverPhone.charAt(0) == '0';

                           transactionService.saveTransaction(
                                   notValidatedTransaction.getEmitter(),
                                   notValidatedTransaction.getReceiver(),
                                   notValidatedTransaction.getAmount()
                           );

                           //making logs for that transaction
                           log.warn(emitter.getPhone()+" has sent "+amount+"DH to "+receiverPhone);

                           notValidatedTransactionService.deleteTransaction(notValidatedTransaction.getId());

                           emitter.setSolde(emitter.getSolde() - amount);

                           if(receiver_is_a_client == true){
                              Client receiver = clientRepository.findClientByPhone(receiverPhone).get();
                              receiver.setSolde(receiver.getSolde() + amount);
                           }

                       } else {
                           throw new RuntimeException("You don't have enough money to do that transaction");
                       }
                   }

                   else {
                       throw new RuntimeException("This transaction is already done");
                   }
               }
           }
       }
       else{
           throw new RuntimeException("There is no transaction with that id");
       }

    }

}

