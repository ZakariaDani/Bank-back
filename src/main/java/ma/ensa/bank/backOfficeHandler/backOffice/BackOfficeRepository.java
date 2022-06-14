package ma.ensa.bank.backOfficeHandler.backOffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface BackOfficeRepository extends JpaRepository<BackOffice, Long> {
    Optional<BackOffice> findByEmail(String email);
}
