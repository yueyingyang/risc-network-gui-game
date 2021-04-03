package edu.duke.ece651.risc.web;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

public class UserDto {
    //@NotNull
    //@NotEmpty
    private String firstName;
    
    //@NotNull
    //@NotEmpty
    private String lastName;
    
    //@NotNull
    //@NotEmpty
    private String password;
    private String matchingPassword;
    
    //@NotNull
    //@NotEmpty
    private String email;
    
    // standard getters and setters
    
    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

}











