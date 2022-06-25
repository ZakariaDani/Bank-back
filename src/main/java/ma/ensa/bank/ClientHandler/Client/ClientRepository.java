package ma.ensa.bank.ClientHandler.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


    @Query("SELECT s FROM Client s WHERE s.phone=?1")
    Optional<Client> findByPhone(String phone);

    @Query("SELECT s FROM Client s WHERE s.email=?1")
    Optional<Client> findClientByEmail(String email);

    Optional<Client> findClientById(Long id);

    @Query("SELECT s FROM Client s WHERE s.phone=?1")
    Optional<Client> findClientByPhone(String phone);

    @Query("SELECT s FROM Client s WHERE s.agent IS NULL")
    Optional<List<Client>> findClientWithoutAgent();

    @Query("SELECT s FROM Client s WHERE s.agent.idCardNumber=?1")
    Optional<List<Client>> getClientsByAgentId(Long agentId);

}