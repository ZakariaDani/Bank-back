package ma.ensa.bank.ClientHandler.Client.TransactionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotValidatedTransactionService {
    @Autowired
    private NotValidatedTransactionRepository notValidatedTransactionRepository;

    public NotValidatedTransaction saveTransaction(String emitter, String receiver, double amount){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            NotValidatedTransaction transaction = new NotValidatedTransaction();
            transaction.setEmitter(emitter);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setDate(dtf.format(now));

            return notValidatedTransactionRepository.save(transaction);
        }
        catch(Exception exception){
            throw new RuntimeException("There is something wrong with the server try later");
        }
    }

    public void deleteTransaction(Long id) {
        try{
            notValidatedTransactionRepository.deleteById(id);

        }
        catch (Exception exception){
            throw new RuntimeException("There is something wrong with the server");
        }
    }
}