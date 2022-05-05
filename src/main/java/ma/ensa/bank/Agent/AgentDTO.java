package ma.ensa.bank.Agent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgentDTO {
    private String idCardNumber;
    private String name;
    private String phone;
    private String email;
    private LocalDate birth;
}
