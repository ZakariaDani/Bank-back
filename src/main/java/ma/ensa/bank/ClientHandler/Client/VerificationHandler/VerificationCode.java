package ma.ensa.bank.ClientHandler.Client.VerificationHandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;
    private String code;
    private String date;
    @ManyToOne()
    @JoinColumn(name="transaction",referencedColumnName = "id")
    private NotValidatedTransaction transaction;
}
