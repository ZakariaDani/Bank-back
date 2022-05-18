package ma.ensa.bank.ClientHandler.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


    Client findByPhone(String phone);

    @Query("SELECT s FROM Client s WHERE s.email=?1")
    Optional<Client> findClientByEmail(String email);

    @Query("SELECT s FROM Client s WHERE s.id=?1")
    Optional<Client> findClientById(Long id);

    @Query("SELECT s FROM Client s WHERE s.phone=?1")
    Optional<Client> findClientByPhone(String phone);

}