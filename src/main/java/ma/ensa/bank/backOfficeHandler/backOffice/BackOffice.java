package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@Data
public class BackOffice implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "back_id", columnDefinition = "VARCHAR(255)")
    private String back_id;

    @Column(unique = true)
    private String email;
    private String password;

    public BackOffice(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
