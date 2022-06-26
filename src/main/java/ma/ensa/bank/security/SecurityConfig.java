package ma.ensa.bank.security;


import ma.ensa.bank.ClientHandler.Client.Client;
import ma.ensa.bank.ClientHandler.Client.ClientService;
import ma.ensa.bank.agentHandler.agent.Agent;
import ma.ensa.bank.agentHandler.agent.AgentService;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import ma.ensa.bank.filter.AuthenticationFilter;
import ma.ensa.bank.filter.AuthorisationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private AgentService agentService;
    @Autowired private BackOfficeService backOfficeService;
    @Autowired private ClientService clientService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user ;

                Pattern pattern_of_a_phone_number = Pattern.compile("^[0-9]+");//. represents single character
                Matcher m = pattern_of_a_phone_number.matcher(username);
                boolean is_a_phone_number = m.matches();

                if(is_a_phone_number){
                    Client client = clientService.getClientByPhone(username);
                    if(client != null){

                        Collection<GrantedAuthority> authorities = new ArrayList<>();
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CLIENT") ;
                        authorities.add(authority);
                        user = new User(username,client.getPassword(),authorities);
                    }
                    else{
                        user = null;
                    }
                }
                else{
                    Agent agent = agentService.getAgentByEmail(username);


                    if(agent != null){
                        Collection<GrantedAuthority> authorities = new ArrayList<>();
                        SimpleGrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_AGENT");
                        authorities.add(roleAuthority);
                        user = new User(agent.getEmail(),agent.getPassword(), authorities );
                    }else {
                        BackOffice backOffice = backOfficeService.getBackOfficeByEmail(username);


                        if(backOffice != null){
                            Collection<GrantedAuthority> authorities = new ArrayList<>();
                            SimpleGrantedAuthority backOfficeAuthority = new SimpleGrantedAuthority("ROLE_BACKOFFICE");
                            authorities.add(backOfficeAuthority);
                            user = new User(backOffice.getEmail(),backOffice.getPassword(), authorities );
                        }
                        else {
                            user = null;
                        }
                    }}
                return user;
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.cors().and().csrf().disable();
        //authentication filters:
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());
        http.cors();

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login","/api/v1/client/register", "/token/refresh/**").permitAll();
        //we will add it later when the front is finished

        /*http.authorizeHttpRequests().anyRequest().authenticated();*/
        http.authorizeHttpRequests().anyRequest().permitAll();
        http.addFilter(authenticationFilter );

        http.addFilterBefore(new AuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}