package ma.ensa.bank.agentHandler.agent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgentDTO {
    private Long idCardNumber;

    private String firstName, lastName, adress, email, phone, matricule, patente, description, fileName, password, backofficeEmail;

    private LocalDate dateOfBirth;

    private boolean isFavorite;
    private Long backofficeId;
    private int number_of_client;

    public AgentDTO(Agent agent) {
        this.idCardNumber = agent.getIdCardNumber();
        this.firstName = agent.getFirstName();
        this.lastName =agent.getLastName();
        this.adress = agent.getAdress();
        this.email = agent.getEmail();
        this.phone = agent.getPhone();
        this.matricule = agent.getMatricule();
        this.patente = agent.getPatente();
        this.description =agent.getDescription();
        this.fileName = agent.getFileName();
        this.password = agent.getPassword();
        this.backofficeEmail = agent.getBackOffice().getEmail();
        this.dateOfBirth = agent.getDateOfBirth();
        this.isFavorite = agent.isFavorite();
        this.backofficeId = agent.getBackOffice().getBackId();
        this.number_of_client = agent.getNumber_of_client();
    }
}
