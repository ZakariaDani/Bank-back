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
@Data
@Table
@AllArgsConstructor
public class Client {

        @Id @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private String address;

        private LocalDate birth;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        private Double solde;
        private Boolean isFavorite=false;

        @ManyToOne(
                cascade = CascadeType.MERGE
        )
        @JoinColumn(name="agent_id")
        private Agent agent;

        public Client(){super();}


        public Client(String fname, String lname, String phone, String email, double solde) {
            this.firstName = fname;
            this.lastName = lname;
            this.phone = phone;
            this.email = email;
            this.solde=solde;
            this.isFavorite=false;
        }

        public Client(String fname, String lname, String phone, String email, String address, LocalDate birth, double solde) {
            this.firstName = fname;
            this.lastName = lname;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.birth = birth;
            this.solde = solde;
        }
}
