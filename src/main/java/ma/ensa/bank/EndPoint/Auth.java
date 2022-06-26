package ma.ensa.bank.EndPoint;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentService;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("token")

public class Auth {
    @Autowired private AgentService agentService;
    @Autowired private BackOfficeService backOfficeService;

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer")) {
            String jwt = authorisationHeader.substring(7);
            try {
                String token = authorisationHeader.substring("Bearer".length()+1);
                Algorithm algorithm = Algorithm.HMAC256("ATLASSEBANK".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                User user;
                Agent agent = agentService.getAgentByEmail(username);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                if(agent != null){
                    SimpleGrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_AGENT");
                    authorities.add(roleAuthority);
                    user = new User(agent.getEmail(),agent.getPassword(), authorities );
                }else {
                    BackOffice backOffice = backOfficeService.getBackOfficeByEmail(username);
                    if(backOffice != null){
                        SimpleGrantedAuthority backOfficeAuthority = new SimpleGrantedAuthority("ROLE_BACKOFFICE");
                        authorities.add(backOfficeAuthority);
                        user = new User(backOffice.getEmail(),backOffice.getPassword(), authorities );
                    }
                    else {
                        user = null;
                    }
                }

                if (user == null)
                    throw new Exception("user not Found");

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 ))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("Access_token", access_token);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens );
            }catch(Exception e) {
                response.setHeader("error", e.getMessage());
                //response.sendError(HttpStatus.FORBIDDEN.value() );
                Map<String, String> tokens = new HashMap<String, String>();
                tokens.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens );
            }
        }
    }
}
