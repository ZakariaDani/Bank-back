package ma.ensa.bank.ClientHandler.Client.TransactionHandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ensa.bank.ClientHandler.Client.VerificationHandler.VerificationCode;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NotValidatedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emitter;
    private String receiver;

    private double amount;

    private String date;

    @OneToMany(mappedBy = "transaction",cascade = CascadeType.ALL, orphanRemoval = true)
    List<VerificationCode> verificationCodeList;
}