package ma.ensa.bank.ClientHandler.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password="123456";
    private String address;
    private Double solde = 0.0;
    private Double plafon = 0.0;

    private Long agentId = 0L;

    public ClientDTO(Client client){
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.phone = client.getPhone();
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.address = client.getFirstName();
        this.solde = client.getSolde();
        this.plafon = client.getPlafon();
    }
}