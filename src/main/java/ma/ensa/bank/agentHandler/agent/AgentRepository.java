package ma.ensa.bank.agentHandler.agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByEmail(String email);

    @Query("SELECT s FROM Agent s WHERE s.email=?1")
    Optional<Agent> findAgentByEmail(String email);

    @Query("SELECT s FROM Agent s WHERE s.idCardNumber=?1")
    Optional<Agent> findAgentById(Long agentIdCard);

    @Query("SELECT s FROM Agent s WHERE s.phone=?1")
    Optional<Agent> findAgentByPhone(String phone);

    @Modifying
    @Query("DELETE FROM Agent a WHERE a.email=:email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT a FROM Agent a WHERE a.isFavorite=true")
    List<Agent> findFavoriteAgents();

}