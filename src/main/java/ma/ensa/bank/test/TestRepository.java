package ma.ensa.bank.test;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRepository extends CrudRepository<Test, UUID> {
}
