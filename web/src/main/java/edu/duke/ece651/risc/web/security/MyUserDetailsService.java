package edu.duke.ece651.risc.web.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private Map<String, User> roles = new HashMap<>();

    @PostConstruct
    public void init() {
        roles.put("user1", new User("user1", passwordEncoder().encode("user1Pass"), getAuthority("ROLE_USER")));
        roles.put("user2", new User("user2", passwordEncoder().encode("user2Pass"), getAuthority("ROLE_USER")));
        roles.put("user3", new User("user3", passwordEncoder().encode("user3Pass"), getAuthority("ROLE_USER")));
        //roles.put("user2", new User("user2", "{noop}user2Pass", getAuthority("ROLE_USER")));
        //roles.put("user3", new User("user3", "{noop}user3Pass", getAuthority("ROLE_USER")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return roles.get(username);
    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }

    
}










