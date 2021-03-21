package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.ClientPlayer;
import edu.duke.ece651.risc.shared.GameMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SpringBootApplication
@RestController
public class WebApplication {

  @Value("${spring.application.server.hostname}")
  String hostname;

  @Value("${spring.application.server.port}")
  Integer port;

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

  @GetMapping
  public String hello() throws IOException {
    Socket s = new Socket(hostname, port);
    ClientPlayer player = new ClientPlayer(new BufferedReader(new InputStreamReader(s.getInputStream())),
            new PrintWriter(s.getOutputStream(), true), null, null);
    return player.recvMessage();
  }
}
