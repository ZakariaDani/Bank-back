package ma.ensa.bank.ClientHandler.Client;

import ma.ensa.bank.Agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


    Client findByPhone(String phone);

    Client findClientByEmail(String email);

    Client findClientById(Long id);

    Client findClientByPhone(String phone);

}