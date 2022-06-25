package ma.ensa.bank.ClientHandler.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByPhone(String phone);
    Optional<Client> findClientByPhone(String phone);

    Optional<Client> findClientByEmail(String email);
    Optional<Client> findClientById(Long id);

    @Query("SELECT s FROM Client s WHERE s.agent IS NULL")
    Optional<List<Client>> findClientWithoutAgent();

    @Query("SELECT s FROM Client s WHERE s.agent.idCardNumber=?1")
    Optional<List<Client>> getClientsByAgentId(Long agentId);

}