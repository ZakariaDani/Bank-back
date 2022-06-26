package ma.ensa.bank.ClientHandler.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDate;

@Entity
@Data
@Table
@ToString
@AllArgsConstructor
public class Client {

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;

    private LocalDate creation_date;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Double solde;
    private Double plafon;
    private Boolean isFavorite=false;

    //this variable is used to impose on the
    //user to enter a new password for the first time
    private Boolean firstTime = false;
    @ManyToOne()
    @JoinColumn(name="agent_id")
    private Agent agent;

    public Client(){super();}

    public Client(ClientDTO clientDTO){

        this.firstName = clientDTO.getFirstName();
        this.lastName = clientDTO.getLastName();
        this.phone = clientDTO.getPhone();
        this.email = clientDTO.getEmail();
        this.password = clientDTO.getPassword();
        this.solde = clientDTO.getSolde();
        this.address = clientDTO.getAddress();
        this.plafon = clientDTO.getPlafon();

    }
}