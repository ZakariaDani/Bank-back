package ma.ensa.bank.Agent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgentDTO {
    private Long idCardNumber;
    private String firstName;
    private String LastName;
    private String adress;
    private String email;
    private String phone;
    private String matricule;
    private String patente;
    private String description;
    private String file;
    private String password;
    private LocalDate dateOfBirth;
}
