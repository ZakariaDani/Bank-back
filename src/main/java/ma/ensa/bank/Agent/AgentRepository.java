package ma.ensa.bank.Agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    Agent findByEmail(String email);

    @Query("SELECT s FROM Agent s WHERE s.email=?1")
    Optional<Agent> findAgentByEmail(String email);

    @Query("SELECT s FROM Agent s WHERE s.idCardNumber=?1")
    Optional<Agent> findAgentById(Long agentIdCard);

    @Query("SELECT s FROM Agent s WHERE s.phone=?1")
    Optional<Agent> findAgentByPhone(String phone);

}
