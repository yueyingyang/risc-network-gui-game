package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.ClientPlayer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerConnectionMapping {

  @Value("${spring.application.server.hostname}")
  String hostname;

  @Value("${spring.application.server.port}")
  Integer port;

  public static Map<String, ClientPlayer> players = new HashMap<>();

  @PostConstruct
  public void init() {
  }

  public ClientPlayer getClientPlayer(String name) throws IOException {
    // if no connection yet
    if (!players.containsKey(name)) {
      Socket s = new Socket(hostname, port);
      ClientPlayer player = new ClientPlayer(new BufferedReader(new InputStreamReader(s.getInputStream())),
              new PrintWriter(s.getOutputStream(), true), null, System.out);
      players.put(name, player);
    }
    return players.get(name);
  }

  public ClientPlayer getTemporaryPlayer() throws IOException {
    Socket s = new Socket(hostname, port);
    return new ClientPlayer(new BufferedReader(new InputStreamReader(s.getInputStream())),
            new PrintWriter(s.getOutputStream(), true), null, System.out);
  }

}
