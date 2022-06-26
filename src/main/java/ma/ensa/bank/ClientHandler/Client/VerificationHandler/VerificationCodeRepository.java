package ma.ensa.bank.ClientHandler.Client.VerificationHandler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    public VerificationCode findVerificationCodeByTransactionIdAndReceiver(Long id , String receiver);

}