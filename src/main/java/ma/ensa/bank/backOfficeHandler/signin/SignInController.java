package ma.ensa.bank.backOfficeHandler.signin;
import lombok.AllArgsConstructor;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.ResponseBackOffice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/backoffice/login")
@AllArgsConstructor
public class SignInController {
    private SignInService signInService;
    @PostMapping
    public ResponseBackOffice login(@RequestBody BackOffice backOffice) {
        if(backOffice.getPassword()=="" || backOffice.getEmail()==""){
            throw new IllegalStateException("Please Fill All Inputs!");
        }
        return signInService.login(backOffice);
    }
}
