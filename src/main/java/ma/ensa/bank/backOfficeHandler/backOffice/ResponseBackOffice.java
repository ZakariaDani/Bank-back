package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.*;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBackOffice {
    private Long backId;
    private String email;
    private String password;
}
