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

			BackOffice backOffice = new BackOffice(null,
					"office@gmail.com",
					"123456",
					"John",
					"Doe",
					"06888888888",
					LocalDate.now(),null
			);
			backOfficeRepository.save(backOffice);


			backOfficeService.saveAgent(new AgentDTO(null,"Zakaria", "Dani", "Kaboul-afghanistan", "zakaria@email.com",
					"0606060606", "E156156", "5556", "good one", "file",
					"Dani1234", backOffice.getEmail(),null ,false,1L, 15));
			backOfficeService.saveAgent(new AgentDTO(null,"Aymane",
					"Daif", "azli", "aymane@email.com", "070707070707", "E933333", "5557",
					"OK", "file", "Daif1234", backOffice.getEmail(),null,false,
					1L ,11));
			backOfficeService.saveAgent(new AgentDTO(null,"moha", "Daif", "azli",
					"moha@email.com", "070707070707", "E933333", "5557", "OK", "file",
					"Daif1234", backOffice.getEmail(),null,false,1L ,20));
			backOfficeService.saveAgent(new AgentDTO(null,"samir", "Daif", "azli",
					"samir@email.com", "070707070707", "E933333", "5557", "OK",
					"file", "Daif1234", backOffice.getEmail(),null,
					false,1L ,
					3));

		};
	}
}
