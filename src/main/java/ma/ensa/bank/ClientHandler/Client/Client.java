package ma.ensa.bank.ClientHandler.Client;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table
@AllArgsConstructor
public class Client {

        @Id
        private String idCardNumber;
        private String fname;
        private String lname;
        private String phone;
        private String email;
        private LocalDate birth;
        private String password;


        public Client(){super();}


        public Client(String fname, String lname, String phone, String email, LocalDate birth, String idCardNumber) {
            this.fname = fname;
            this.lname = lname;
            this.phone = phone;
            this.email = email;
            this.birth = birth;
            this.idCardNumber = idCardNumber;
        }


}
