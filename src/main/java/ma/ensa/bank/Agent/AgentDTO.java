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
    private String firstName, LastName, adress, email, phone, matricule, patente, description, file, password;
    private LocalDate dateOfBirth;
}
