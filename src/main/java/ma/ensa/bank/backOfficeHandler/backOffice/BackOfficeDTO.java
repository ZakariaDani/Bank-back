package ma.ensa.bank.backOfficeHandler.backOffice;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class BackOfficeDTO {
    private Long backId;
    private String email, password, firstName, lastName, phone;
    private LocalDate dateOfBirth;
}
