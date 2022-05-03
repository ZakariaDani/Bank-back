package ma.ensa.bank.backOfficeHandler.signin;

import lombok.AllArgsConstructor;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import ma.ensa.bank.backOfficeHandler.backOffice.ResponseBackOffice;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInService {
    private final BackOfficeService backOfficeService;

    public ResponseBackOffice login(BackOffice backOffice) {
        return backOfficeService.signin(backOffice);
    }
}
