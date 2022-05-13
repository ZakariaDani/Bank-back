package ma.ensa.bank.Agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Table
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCardNumber;
    private String firstName, LastName, adress, email, phone, matricule, patente, description, file;
    private LocalDate dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="backoffice_id")
    private BackOffice backOffice;

    public Agent(){super();}


    public Agent(String firstName, String phone, String email, LocalDate dateOfBirth, Long idCardNumber) {
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.idCardNumber = idCardNumber;
    }

    public Agent(Long idCardNumber, String firstName, String password, String phone, String email, LocalDate dateOfBirth) {
        this.idCardNumber = idCardNumber;
        this.firstName = firstName;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;

    }
}
