package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.*;
import ma.ensa.bank.Agent.Agent;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class BackOffice implements Serializable {
    @Id
    @Column(name = "back_id", nullable = false)
    private String backId;

    @Column(unique = true)
    private String email;
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


}
