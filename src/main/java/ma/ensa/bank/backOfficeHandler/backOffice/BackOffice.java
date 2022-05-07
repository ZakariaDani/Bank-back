package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.*;
import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;


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
    @Column(name = "back_id", nullable = false)
    private String backId;

    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "backoffice_id")
    private List<Agent> agents = new ArrayList<>();



    public BackOffice(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public BackOffice(String backId, String email, String password) {
        this.backId = backId;
        this.email = email;
        this.password = PasswordEncoder.bCryptPasswordEncoder().encode(password);
    }

}
