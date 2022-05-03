package ma.ensa.bank.backOfficeHandler.backOffice;


import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BackOfficeService {

    private final BackOfficeRepository backOfficeRepository;

    public ResponseBackOffice signin(BackOffice backOffice) {
        boolean present1 = backOfficeRepository.findByEmail(backOffice.getEmail()).isPresent();
        if (present1) {
            BackOffice value = backOfficeRepository.findByEmail(backOffice.getEmail()).get();
            ResponseBackOffice responseBackOffice = new ResponseBackOffice();
            BeanUtils.copyProperties(responseBackOffice, value);
            backOfficeRepository.save(value);
            return responseBackOffice;
        }
        else {
            throw new IllegalStateException("invalid");
        }
    }
}