package ma.ensa.bank.ClientHandler.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.Transaction;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.TransactionDTO;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;
import ma.ensa.bank.Helpers.CurrentUserInfo;
import ma.ensa.bank.SMS.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private SmsService smsService;

    

    @CrossOrigin
    @GetMapping("/getInfo")
    public Client getClient(HttpServletRequest request){

        String phone = CurrentUserInfo.getEmail(request);
        return clientService.getClient(phone);
    }

    @CrossOrigin
    @PostMapping("/make_transaction")
    @ResponseBody
    public Long makeTransaction(@RequestBody TransactionDTO transactionDTO ,HttpServletRequest request){

        String receiverPhone = transactionDTO.getReceiverPhone();
        double amount = transactionDTO.getAmount();

        String currentUserPhone = CurrentUserInfo.getEmail(request);

        Long transactionId = clientService.makeTransaction(currentUserPhone, receiverPhone, amount);

        return transactionId;
}
    @CrossOrigin
    @GetMapping("/clients")
    public List<Client> getClients(){
        return clientService.getClients();
    }

    @CrossOrigin
    @PostMapping("/addclient")
    public void addClient(@RequestBody ClientDTO client){
        if(client==null){
            throw new IllegalStateException("No client to add");
        }else{
            clientService.addClient(client);
        }
    }

    @CrossOrigin
    @PutMapping(value="/updateclient/{ClientId}")
    public void updateClient(@PathVariable("ClientId") Long ClientId,@RequestBody Client client){
        if(client==null){
            throw new IllegalStateException("No client to update");
        }else{
            clientService.updateClient(ClientId,client);
        }
    }

    @CrossOrigin
    @DeleteMapping(value="/deleteclient/{clientid}")
    public void deleteagent(@PathVariable("clientid") Long clientid){
        if(clientid==null){
            throw new IllegalStateException("Please Enter a valid CardId");
        }else {
            clientService.deleteClient(clientid);
        }
    }

    @CrossOrigin
    @PostMapping("/make_telecom_recharge")
    @ResponseBody
    public Long makeTelecomRecharge(@RequestBody TransactionDTO transactionDTO ,HttpServletRequest request){

        String entreprise = transactionDTO.getTelecomEntreprise();
        double amount = transactionDTO.getAmount();
        String currentUserPhone = CurrentUserInfo.getEmail(request);

        Long transactionId = clientService.makeTelecomRecharge(currentUserPhone, entreprise, amount);

        return transactionId;
    }

    @CrossOrigin
    @PostMapping("/verification_code")
    @ResponseBody
    public void receive_verification_code(@RequestBody VerificationCode verificationCode,
                                          HttpServletRequest request){

        String phoneNumber = CurrentUserInfo.getEmail(request);
        String id = request.getHeader("transactionId");
        Long transactionId = Long.valueOf(id);

        clientService.receive_verification_code(transactionId, verificationCode.getCode(), phoneNumber);
    }

    @CrossOrigin
    @GetMapping("/getTransactions")
    @ResponseBody
    public List<Transaction> getTranactions(HttpServletRequest request){

        String phoneNumber = CurrentUserInfo.getEmail(request);
        return clientService.getTransactions(phoneNumber);

    }

}
