package ma.ensa.bank.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EmailEntity {
    private String receiver;
    private String subject;
    private String msg;
}