package ma.ensa.bank.ClientHandler.Client.TransactionHandler;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public Page<Transaction> findByEmitterOrReceiver(String emitter, String receiver, Pageable pageable);
}
