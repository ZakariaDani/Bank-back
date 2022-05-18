package ma.ensa.bank;



import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.Agent.AgentDTO;
import ma.ensa.bank.Agent.AgentRepository;
import ma.ensa.bank.Agent.AgentService;

import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeRepository;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import ma.ensa.bank.backOfficeHandler.backOfficeSecurity.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BankBackApplication {
	@Autowired private BackOfficeRepository backOfficeRepository;
	@Autowired private AgentRepository agentRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(BankBackApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AgentService agentService, ClientService clientService){
		return args -> {
//			agentService.addAgent(new Agent(
//					null,
//					"agent1",
//					"1234",
//					"0696785179",
//					"agen1@gmail.com",
//					LocalDate.of(2000, 02, 04))
//			);
//			agentService.addAgent(new Agent(
//					null,
//					"agent2",
//					"1234",
//					"0696785579",
//					"agen2@gmail.com",
//					LocalDate.of(1999, 05, 04))
//			);
//			clientService.addClient(new Client(null,
//					"ahmed",
//					"zibout",
//					"0685412369",
//					"ahmed@gmail.com",
//					LocalDate.of(2000, 03, 05),
//					"15963",
//					150.6));



			BackOffice backOffice = new BackOffice("1",
					"office@gmail.com",
					"123456"
			);
			backOfficeRepository.save(backOffice);

		};
	}
}
