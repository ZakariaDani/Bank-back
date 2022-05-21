package ma.ensa.bank.backOfficeHandler.backOffice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "back_office")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class BackOffice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "back_id", nullable = false)
    private Long backId;

    @Column(name = "email",unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "backoffice_id")
    private List<Agent> agents = new ArrayList<>();
    


    public BackOffice(String email, String password) {
        this.email = email;
        this.password = PasswordEncoder.bCryptPasswordEncoder().encode(password);
    }
    public BackOffice(Long backId, String email, String password) {
        this.backId = backId;
        this.email = email;
        this.password = PasswordEncoder.bCryptPasswordEncoder().encode(password);
    }


}
