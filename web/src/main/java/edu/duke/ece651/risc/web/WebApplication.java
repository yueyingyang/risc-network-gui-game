package edu.duke.ece651.risc.web;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebApplication implements ApplicationRunner {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments arg) throws Exception {
    // create a global bean
  }

}
