package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.bank.Agent.Agent;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BackOffice implements Serializable {
    @Id
    @Column(name = "back_id", nullable = false)
    private UUID back_id;

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
