package ma.ensa.bank.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ClientDTO {
    private String idCardNumber;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private LocalDate birth;
}
