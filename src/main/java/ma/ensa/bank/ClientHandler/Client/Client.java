package ma.ensa.bank.ClientHandler.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table
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

        //pass just for test !!!!!
        private String password="123456";
        private double solde;


        public Client(){super();}


        public Client(String fname, String lname, String phone, String email, double solde) {
            this.fname = fname;
            this.lname = lname;
            this.phone = phone;
            this.email = email;
            this.solde=solde;
        }

        public Client(String fname, String lname, String phone, String email, String address, LocalDate birth, double solde) {
            this.fname = fname;
            this.lname = lname;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.birth = birth;
            this.solde = solde;
        }
}
