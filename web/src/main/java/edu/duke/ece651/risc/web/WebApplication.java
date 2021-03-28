package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class WebApplication implements ApplicationRunner {

  @Value("${spring.application.server.hostname}")
  String hostname;

  @Value("${spring.application.server.port}")
  Integer port;

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments arg) throws Exception {
    // create a global bean
  }

}
