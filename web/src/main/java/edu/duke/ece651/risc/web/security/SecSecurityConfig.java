package edu.duke.ece651.risc.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;



@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter{
    public SecSecurityConfig() {
        super();
     }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/anonymous*").anonymous()
        .antMatchers("/login*").permitAll()
        .antMatchers("/resources/**").permitAll()
        .anyRequest().authenticated()
        .and()
        // ...
        .formLogin()
        .loginPage("/login")
        .successHandler(myAuthenticationSuccessHandler())
        //.defaultSuccessUrl("/lobby?name=test", true)
        .failureUrl("/login?error=true")
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/");
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }


}










