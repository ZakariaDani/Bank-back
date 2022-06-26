package ma.ensa.bank.SMS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SmsEntity {
    private String smsReceiver;
    private String message;
    private String code;
}