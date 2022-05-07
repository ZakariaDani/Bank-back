package ma.ensa.bank;

import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeRepository;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankBackApplication {
	@Autowired
	private BackOfficeRepository backOfficeRepository;

	public static void main(String[] args) {
		SpringApplication.run(BankBackApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args->{
			BackOffice backOffice = new BackOffice("123", "zakaria", "zakaria");
			backOfficeRepository.save(backOffice);
		};
	}
}
