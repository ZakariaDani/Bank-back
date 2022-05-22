package ma.ensa.bank.ClientHandler.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDTO {
    private Long id;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String password;
    private String address;
    private LocalDate birth;
    private double solde;
}
