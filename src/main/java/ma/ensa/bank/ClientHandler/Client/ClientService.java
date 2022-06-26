package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransactionService;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.Transaction;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.TransactionService;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCodeService;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentRepository;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import ma.ensa.bank.email.EmailService;
import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private JavaMailSender javaMailSender;

    @Autowired
    public ClientService(ClientRepository clientRepository,AgentRepository agentRepository, EmailService emailService) {
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
        this.emailService = emailService;
    }

    public void registerClient(ClientDTO clientDTO) {

        this.verify_client_info(clientDTO,"client");
        clientDTO.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(clientDTO.getPassword()));

        Client client = new Client(clientDTO);
        client.setCreation_date(LocalDate.now());

        clientRepository.save(client);

    }
    public void verify_client_info(ClientDTO clientDTO, String actionner) {
        Client opt1 = clientRepository.findClientByPhone(clientDTO.getPhone());
        Client opt2 = clientRepository.findClientByEmail(clientDTO.getEmail());
        Pattern phonePattern = Pattern.compile("^0[5-7][0-9]+$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namesPattern = Pattern.compile("^[A-Za-z][A-Za-z ]{3,20}");
        if (opt1!=null || opt2!=null) {
            throw new IllegalStateException("duplicate data in the database!!");
        }
        else{
            boolean addressRequired = actionner.equals("agent")? true : false;
            boolean client_address_empty;

            if(addressRequired){
                client_address_empty = clientDTO.getAddress().trim().isEmpty();
            }
            else{
                client_address_empty = false;
            }
            boolean there_is_empty_field = clientDTO.getEmail().trim().isEmpty() || clientDTO.getPhone().trim().isEmpty() ||
                    clientDTO.getFirstName().trim().isEmpty() || clientDTO.getLastName().trim().isEmpty()
                    || client_address_empty ;

            if(there_is_empty_field ){
                throw new IllegalStateException("All fields are required!");
            }
            else{
                if (!phonePattern.matcher(clientDTO.getPhone()).matches()) {
                    throw new IllegalStateException("Phone number is not valid!!");
                }
                if (!emailPattern.matcher(clientDTO.getEmail()).matches()) {
                    throw new IllegalStateException("Email format is not valid!!");
                }
                if (!namesPattern.matcher(clientDTO.getFirstName()).matches()) {
                    throw new IllegalStateException("First name is not valid");
                }
                if (!namesPattern.matcher(clientDTO.getLastName()).matches()) {
                    throw new IllegalStateException("Last name is not valid");
                }
            }
        }
    }

    public void addClient(ClientDTO clientDTO){

        this.verify_client_info(clientDTO,"agent");

        clientDTO.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(clientDTO.getPassword()));
        Client client = new Client(clientDTO);

        client.setCreation_date(LocalDate.now());
        client.setFirstTime(true);

        Agent agent = agentRepository.findAgentById(clientDTO.getAgentId()).get();
        client.setAgent(agent);

        List<Client> newList = agent.getClients();
        newList.add(client);
        agent.setClients(newList);
        agent.setNumber_of_client(agent.getClients().toArray().length);
        clientRepository.save(client);

        agentRepository.save(agent);
        sendCodeToClient(client);

    }
    @Transactional
    public void updateClient(Long clientId, ClientDTO clientDTO){

        Pattern phonePattern = Pattern.compile("^0[5-7][0-9]+$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern namesPattern = Pattern.compile("^[A-Za-z]{3,20}$");

        Client clientdb = clientRepository.findClientById(clientId);
        if(!phonePattern.matcher(clientDTO.getPhone()).matches()){
            throw new IllegalStateException("Phone number is not valid");
        }
        if(!emailPattern.matcher(clientDTO.getEmail()).matches()){
            throw new IllegalStateException("Email format is not valid!!");
        }
        if(!namesPattern.matcher(clientDTO.getFirstName()).matches()){
            throw new IllegalStateException("First name is not valid");
        }
        if(!namesPattern.matcher(clientDTO.getLastName()).matches()){
            throw new IllegalStateException("Last name is not valid");
        }
        if(clientDTO.getFirstName()!=null && clientDTO.getFirstName().length()>3 && !Objects.equals(clientdb.getFirstName(),
                clientDTO.getFirstName())){
            clientdb.setFirstName(clientDTO.getFirstName());
        }
        if(clientDTO.getLastName()!=null && clientDTO.getLastName().length()>3 && !Objects.equals(clientdb.getLastName(),clientDTO.getLastName())){
            clientdb.setLastName(clientDTO.getLastName());
        }
        if(clientDTO.getEmail()!=null && clientDTO.getEmail().length()>7 && !Objects.equals(clientdb.getEmail(),
                clientDTO.getEmail())){
            Client opt = clientRepository.findClientByEmail(clientDTO.getEmail());
            if(opt!=null){
                throw new IllegalStateException("email you want to update already exist!!");
            }
            clientdb.setEmail(clientDTO.getEmail());
        }
        if(clientDTO.getPhone()!=null && clientDTO.getPhone().length()>9 &&
                !Objects.equals(clientdb.getPhone(),clientDTO.getPhone())){
            Client opt = clientRepository.findClientByPhone(clientDTO.getPhone());
            if(opt!=null){
                throw new IllegalStateException("phone you want to update already exist!!");
            }
            clientdb.setPhone(clientDTO.getPhone());
        }
        if(clientDTO.getSolde()!=null){
            clientdb.setSolde(clientDTO.getSolde());
        }

        clientRepository.save(clientdb);
    }
    @Transactional
    public void toggleIsFavorite(Long id){
        Client clientDb = clientRepository.findClientById(id);
        clientDb.setIsFavorite(!clientDb.getIsFavorite());
    }
    @Transactional
    public void assign_client_to_agent(Long idClient,String emailAgent){
        Client clientDb = clientRepository.findClientById(idClient);
        Agent agent = agentRepository.findAgentByEmail(emailAgent).get();
        int old = agent.getClients().toArray().length;
        agent.setNumber_of_client(++old);
        clientDb.setAgent(agent);
        clientDb.setFirstTime(true);

        List<Client> newList = agent.getClients();
        newList.add(clientDb);
        agent.setClients(newList);
        sendCodeToClient(clientDb);
    }
    public void sendCodeToClient(Client client){
        String newPassword = new Random().ints(10,
                        33,
                        122).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

        client.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode(newPassword));
        this.updateClient(client.getId(), new ClientDTO(client));

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ebanking159@gmail.com");
            message.setTo(client.getEmail());
            message.setText("Hello Ms, Mr " + client.getFirstName() +" "+ client.getLastName()+
                    " Your New Password is: " + newPassword);
            message.setSubject("E banking: Your new code");

            javaMailSender.send(message);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("there is something wrong with sending email service");
        }
    }
    @Transactional
    public void deleteClient(Long id){
        Client opt1 = clientRepository.findClientById(id);
        if(opt1 != null){
            Client client = opt1;
            Optional<Agent> opt2 = agentRepository.findAgentById(client.getAgent().getIdCardNumber());
            if(opt1.getAgent()!=null) {
                List<Client> newList = opt2.get().getClients();
                newList.remove(client);
                opt2.get().setNumber_of_client(newList.toArray().length);
                opt2.get().setClients(newList);
                clientRepository.deleteById(id);
            }
        }else {
            throw new IllegalStateException("This Id doesn't exist!");
        }
    }
    public List<Client> getClients(){
        return clientRepository.findAll();
    }
    public List<Client> getClientsWithoutAgent(){
        return clientRepository.findByAgent(null);
    }
    public Client getClientByPhone(String phone){
        return clientRepository.findClientByPhone(phone);
    }

    public Client getClientById(Long Id){ return clientRepository.findClientById(Id);};

    public List<Client> getClientsByAgentId(Long agentId){
        return clientRepository.findByAgentIdCardNumber(agentId);
    }

    @Transactional
    public Long makeTransaction(String emitterPhone, String receiverPhone, double amount){

        Pattern p = Pattern.compile("^0[5-7][0-9]+");//. represents single character
        Matcher m = p.matcher(receiverPhone);
        boolean validPhoneNumber = m.matches();

        if( validPhoneNumber == true && amount>0 ){
            try{
                Client emitter  = clientRepository.findClientByPhone(emitterPhone);
                Client receiver  = clientRepository.findClientByPhone(receiverPhone);
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
            Client emitter = clientRepository.findClientByPhone(emitterPhone);
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
                        Client emitter = clientRepository.findClientByPhone(notValidatedTransaction.getEmitter());
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
                                Client receiver = clientRepository.findClientByPhone(receiverPhone);
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

    public boolean getClientStatus(String phoneNumber){

        Client client =  clientRepository.findClientByPhone(phoneNumber);
        if(client != null){
            return client.getFirstTime();
        }
        else{
            throw  new RuntimeException("There is something wrong with the token");
        }
    }

    public List<Transaction> getTClientTransactions(Long id){
        Client client = clientRepository.findClientById(id);
        if(client == null){
            throw new RuntimeException("This client doesn't exit");
        }
        else{
            return transactionService.getTransactions(client.getPhone());
        }
    }
    public void changeClientPassword(String phoneNumber , String newPassword){

        if(newPassword.length() <6){
            throw new RuntimeException("The password must contains at least 6 caracteres");
        }
        else {
            Client clientdb = this.clientRepository.findClientByPhone(phoneNumber);

            if(clientdb == null){
                throw new RuntimeException("This client doesn't exist");
            }
            else{
                clientdb.setPassword(
                        PasswordEncoder.bCryptPasswordEncoder().encode(newPassword)
                );
                clientdb.setFirstTime(false);

                clientRepository.save(clientdb);
            }
        }

    }

    public Client getClientInfo(String phoneNumber){
        Client client = this.clientRepository.findClientByPhone(phoneNumber);
        if(client != null){
            return client;
        }
        else{
            throw new RuntimeException("This client doesn't exist");
        }
    }
}
