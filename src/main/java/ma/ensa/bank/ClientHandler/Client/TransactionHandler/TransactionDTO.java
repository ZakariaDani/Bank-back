package ma.ensa.bank.ClientHandler.Client.TransactionHandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String receiverPhone;
    private String telecomEntreprise;
    private double amount;
}