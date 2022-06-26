package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.agentHandler.agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByPhone(String phone);

    Client findClientByEmail(String email);

    Client findClientById(Long id);

    List<Client> findByAgent(Agent agent);

    List<Client> findByAgentIdCardNumber(Long agentId);



}