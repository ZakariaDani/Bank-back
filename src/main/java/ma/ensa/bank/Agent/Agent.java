package ma.ensa.bank.Agent;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table
public class Agent {
    @Id
    private String idCardNumber;
    private String name;
    private String phone;
    private String email;
    private LocalDate birth;


    public Agent(){super();}


    public Agent(String name, String phone, String email, LocalDate birth, String idCardNumber) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
        this.idCardNumber = idCardNumber;
    }
}
