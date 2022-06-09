package ma.ensa.bank.Sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsEntity {
    private String FROM;
    private String TO_NUMBER;
    private String MESSAGE;
}
