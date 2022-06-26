package ma.ensa.bank.backOfficeHandler.backOffice;

import lombok.AllArgsConstructor;
import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentDTO;
import ma.ensa.bank.agentHandler.agent.AgentService;
import ma.ensa.bank.image.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/backoffice")
public class  BackOfficeController {
    private final BackOfficeService backOfficeService;
    private final AgentService agentService;
    private final ImageService imageService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getBackOffice(@PathVariable("email") String email) {
        BackOffice backOffice = backOfficeService.getBackOfficeByEmail(email);
        if(backOffice != null) {
            return ResponseEntity.status(HttpStatus.OK).body(backOffice);
        } else {
            return ResponseEntity.badRequest().body("There is no backoffice with that specific email");
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<?> updateBackOffice(@PathVariable("email") String email, @RequestBody BackOfficeDTO backOfficeDTO) {
        if (backOfficeDTO == null)
            return ResponseEntity.badRequest().body("The provided backoffice is not valid");

        BackOffice existedBackOffice = backOfficeService.getBackOfficeByEmail(email);
        if(existedBackOffice != null) {
            BackOffice newBackOffice = backOfficeService.updateBackOffice(existedBackOffice, backOfficeDTO);
            return ResponseEntity
                    .ok()
                    .body(newBackOffice);
        } else {
            return ResponseEntity.badRequest().body("There is no backoffice with that specific email");
        }
    }


    @GetMapping("/agents")
    public ResponseEntity<List<Agent>> getAgents() {
        return new ResponseEntity<>(backOfficeService.getAllAgents(), HttpStatus.OK);
    }

    @GetMapping("/agents/favorites")
    public ResponseEntity<List<Agent>> getFavoriteAgents(){
        return new ResponseEntity<>(backOfficeService.getFavoriteAgents(), HttpStatus.OK);
    }

    @GetMapping("/agents/{id}")
    public ResponseEntity<?> getAgent(@PathVariable("id") final Long id) {
        Optional<Agent> agent = backOfficeService.getAgentById(id);
        if(agent.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(agent.get());
        } else {
            return ResponseEntity.badRequest().body("There is no agent with that specific id");
        }
    }

    @PostMapping("/agents")
    public ResponseEntity<?> createAgent(@RequestBody AgentDTO agentDTO) {
        if (agentDTO == null)
            return ResponseEntity.badRequest().body("The provided agent is not valid");
        Agent newAgent = backOfficeService.saveAgent(agentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newAgent);
    }

     
    
    @PostMapping("/agents/{id}/image")
    public ResponseEntity<?> createAgentImage(@RequestParam MultipartFile file, @PathVariable("id") final Long id) {
       try {
            String imagePath = this.imageService.uploadImage(file);
	    this.backOfficeService.saveAgentImage(id, StringUtils.cleanPath(file.getOriginalFilename()));
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(imagePath);
        } catch (Exception e) {
		    return null;
        }
    }


    @PatchMapping ("/agents/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable("id") final Long id, @RequestBody AgentDTO agentDTO) {
        if (agentDTO == null)
            return ResponseEntity.badRequest().body("The provided agent is not valid");

        Optional<Agent> existedAgent = backOfficeService.getAgentById(id);
        if(existedAgent.isPresent()) {
            Agent currentAgent = existedAgent.get();

            Agent newAgent = backOfficeService.updateAgent(currentAgent, agentDTO);
            return ResponseEntity
                    .ok()
                    .body(newAgent);
        } else {
            return ResponseEntity.badRequest().body("There is no agent with that specific id");
        }
    }

    @PatchMapping ("/agents/{id}/favorite")
    public ResponseEntity<?> updateFavoriteAgent(@PathVariable("id") final Long id, @RequestBody AgentDTO agentDTO) {
        if (agentDTO == null)
            return ResponseEntity.badRequest().body("The provided agent is not valid");

        Optional<Agent> existedAgent = backOfficeService.getAgentById(id);
        if(existedAgent.isPresent()) {
            Agent currentAgent = existedAgent.get();

            Agent newAgent = backOfficeService.updateFavoriteAgent(currentAgent, agentDTO);
            return ResponseEntity
                    .ok()
                    .body(newAgent);
        } else {
            return ResponseEntity.badRequest().body("There is no agent with that specific id");
        }
    }


    @DeleteMapping("/agents/{agentEmail}")
    public ResponseEntity<?> deleteAgent(@PathVariable("agentEmail") String agentEmail) {
        if (agentEmail == null)
            return ResponseEntity.badRequest().body("The given agent is not valid");
        if (agentService.getAgentByEmail(agentEmail) == null)
            return ResponseEntity.badRequest().body("The given agent is not valid");
        backOfficeService.deleteAgent(agentEmail);
        return ResponseEntity.ok().body(agentService.getAgentByEmail(agentEmail));
    }


    @GetMapping(
            value = "/agents/image/{imageName}",
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE}
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("imageName") String fileName) throws IOException {
        return this.imageService.getImage(fileName);
    }
}
