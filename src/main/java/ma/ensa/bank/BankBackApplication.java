package ma.ensa.bank;



import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentDTO;
import ma.ensa.bank.agentHandler.agent.AgentRepository;

import ma.ensa.bank.ClientHandler.Client.ClientDTO;
import ma.ensa.bank.ClientHandler.Client.ClientRepository;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeRepository;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class BankBackApplication {
	@Autowired private BackOfficeRepository backOfficeRepository;
	@Autowired private AgentRepository agentRepository;
	@Autowired private ClientRepository clientRepository;
	@Autowired private BackOfficeService backOfficeService;
	@Autowired private ClientService clientService;

	public static void main(String[] args) {
		SpringApplication.run(BankBackApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner( ClientService clientService){
		return args -> {


//			clientService.addClient(new Client(null,
//					"ahmed",
//					"zibout",
//					"0685412369",
//					"ahmed@gmail.com",
//					LocalDate.of(2000, 03, 05),
//					"15963",
//					150.6));
			//clientService.addClient(new Client("Marouane","Zibout","0625252528","maoruane@email.com","Mahmid", LocalDate.of(2000, 03, 05),100.00));

			//clientService.addClient(new Client("Marouane","Zibout","0625252528","maoruane@email.com","Mahmid", LocalDate.of(2000, 03, 05),100.00));

			/*clientService.addClient(
					new Client(
							"abdo",
							"elhammadi",
							"0666",
							"a@gmail.com",
					200));*/


			BackOffice backOffice = new BackOffice(null,
					"office@gmail.com",
					"123456",
					"John",
					"Doe",
					"2126888888888",
					LocalDate.now(),null
			);
			backOfficeRepository.save(backOffice);

		//	clientService.addClient(new ClientDTO(null, "zakaria", "dani","0606060606", "email@email.com", "789", "ta7nawt", LocalDate.of(2000,5,12),200.0,2L));
		//	clientService.addClient(new ClientDTO(null, "marouane", "zibout","0606780606", "client@email.com", "789", "ta7nawt", LocalDate.of(2000,5,12),200.0,null));

		};
	}
}
