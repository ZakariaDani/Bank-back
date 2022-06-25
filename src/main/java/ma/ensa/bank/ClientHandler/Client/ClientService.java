package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;
import  ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransactionService;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.Transaction;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.TransactionService;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCodeService;
import ma.ensa.bank.Helpers.CurrentUserInfo;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientRepository clientRepository;

    @Autowired
    private NotValidatedTransactionService notValidatedTransactionService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private VerificationCodeService verificationCodeService;

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
        boolean there_is_a_null_attribute = client.getPhone()==null|| client.getLname()==null||
                    client.getFname()==null|| client.getEmail()==null;

        if(opt1!=null || opt2 != null ){
            throw new IllegalStateException("Client already exist!!");
        }

        else if(there_is_a_null_attribute == true){
            throw new IllegalStateException("All the fields should be filled in");
        }
        else{
            System.out.println(client);
            client.setSolde(0);
            client.setPassword(PasswordEncoder.bCryptPasswordEncoder().encode("123456"));
            client.setBirth(LocalDate.now());
            clientRepository.save(client);
        }
    }

    public void updateClient(@RequestBody Client client,String email){

        Client clientdb = clientRepository.findClientByEmail(email);
        boolean there_is_a_problem = false;

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

        if(there_is_a_problem != false){

        }
    }

    public Client getClient(String phone){
        try{
            Client client = clientRepository.findClientByPhone(phone);

            return client;
        }
        catch(RuntimeException exception){
            throw exception;
        }
    }
    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone);
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
            Client emitter = clientRepository.findClientByPhone(emitterPhone);
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
                       Client emitter = clientRepository.findClientByPhone(notValidatedTransaction.getEmitter());
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
                              Client receiver = clientRepository.findClientByPhone(receiverPhone);
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

