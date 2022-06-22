package ma.ensa.bank.ClientHandler.Client.TransactionHandler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void saveTransaction(String emitter, String receiver, double amount){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Transaction transaction = new Transaction();
            transaction.setEmitter(emitter);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setDate(dtf.format(now));

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
