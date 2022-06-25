package ma.ensa.bank.ClientHandler.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password="123456";
    private String address;
    private LocalDate birth;
    private Boolean isFavorite=false;
    private Double solde;
    private Long agentId;

    public ClientDTO(Long id, String firstName, String lastName, String phone, String email, String password, String address, LocalDate birth, Double solde,Long agentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.birth = birth;
        this.solde = solde;
        this.agentId = agentId;
    }
}