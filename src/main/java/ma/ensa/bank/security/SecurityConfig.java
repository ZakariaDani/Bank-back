package ma.ensa.bank.security;


import ma.ensa.bank.Agent.Agent;
import ma.ensa.bank.Agent.AgentService;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOffice;
import ma.ensa.bank.backOfficeHandler.backOffice.BackOfficeService;
import ma.ensa.bank.filter.AuthenticationFilter;
import ma.ensa.bank.filter.AuthorisationFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private AgentService agentService;
    @Autowired private BackOfficeService backOfficeService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user ;
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
                }
                return user;
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authentication filters:
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login", "/token/refresh/**").permitAll();
        //we will add it later when the front is finished
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter );
        http.addFilterBefore(new AuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
