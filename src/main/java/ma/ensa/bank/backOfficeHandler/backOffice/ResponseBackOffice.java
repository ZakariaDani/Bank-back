package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBackOffice {
    private String back_id;
    private String name;
    private String email;
}
