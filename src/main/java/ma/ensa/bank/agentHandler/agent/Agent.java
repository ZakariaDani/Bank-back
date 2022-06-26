package ma.ensa.bank.agentHandler.agent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id", nullable = false)
    private Long idCardNumber;
    private String firstName;
    private String LastName;
    private String adress;
    private String email;
    private String phone;
    private String matricule;
    private String patente;
    private String description;
    private String fileName;
    private LocalDate dateOfBirth;
    private boolean isFavorite;
    private int number_of_client=0;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name="backoffice_id")
    private BackOffice backOffice;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            mappedBy = "agent")
    private List<Client> Clients = new ArrayList<>();


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

    public Agent(String firstName, String lastName, String address, String email, String phone, String matricule, String patente, String description, String fileName, LocalDate dateOfBirth, String password,boolean isFavorite) {
        this.firstName = firstName;
        LastName = lastName;
        this.adress = address;
        this.email = email;
        this.phone = phone;
        this.matricule = matricule;
        this.patente = patente;
        this.description = description;
        this.fileName = fileName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.isFavorite = isFavorite;
    }
}