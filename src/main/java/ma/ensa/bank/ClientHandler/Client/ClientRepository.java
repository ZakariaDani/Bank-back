package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.Agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Client findByPhone(String phone);

    @Query("SELECT s FROM Client s WHERE s.email=?1")
    Optional<Client> findClientByEmail(String email);

    @Query("SELECT s FROM Client s WHERE s.idCardNumber=?1")
    Optional<Client> findClientById(String agentIdCard);

    @Query("SELECT s FROM Client s WHERE s.phone=?1")
    Optional<Client> findClientByPhone(String phone);

}