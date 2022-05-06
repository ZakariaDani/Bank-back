package ma.ensa.bank;

import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankBackApplication.class, args);
	}

}
