package ma.ensa.bank.ClientHandler.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDate;

@Entity
@Table
@Data
@AllArgsConstructor
public class Client {

        @Id @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String fname;
        private String lname;
        private String phone;
        private String email;
        private String address;

        private LocalDate birth;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;
        private double solde;
        private double plafon;
        private Boolean isFavorite=false;

        @ManyToOne(
                cascade = CascadeType.MERGE
        )
        @JoinColumn(name="agent_id")
        private Agent agent;

        public Client(){super();}

        public Client(String fname, String lname, String phone,
                      String email, double solde,double plafon, String address, boolean isFavorite
        ) {
            this.fname = fname;
            this.lname = lname;
            this.phone = phone;
            this.email = email;
            this.solde = solde;
            this.plafon = plafon;
            this.address = address;
            this.isFavorite = isFavorite;

        }

}
