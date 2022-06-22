package ma.ensa.bank.ClientHandler.Client.TransactionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    Logger log = LoggerFactory.getLogger(TransactionService.class);

    public void saveTransaction(String emitter, String receiver, double amount){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Transaction transaction = new Transaction();
            transaction.setEmitter(emitter);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setDate(dtf.format(now));

            log.info(emitter+"has sent "+amount+"DH to "+receiver);
            transactionRepository.save(transaction);

        }
        catch(Exception exception){
            throw new RuntimeException("There is something wrong with the server try later");
        }
    }

    public Page<Transaction> getTransactions(String currentUserPhoneNumber, int page, int pageSize){

        return transactionRepository.findByEmitterOrReceiver(currentUserPhoneNumber,
                currentUserPhoneNumber, PageRequest.of(page,pageSize, Sort.by("id").descending()));

    }
}
