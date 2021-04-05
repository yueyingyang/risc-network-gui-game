package edu.duke.ece651.risc.web;

import edu.duke.ece651.risc.shared.ClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerSocketMap {
  Logger logger = LoggerFactory.getLogger(PlayerSocketMap.class);
  @Value("${spring.application.server.hostname}")
  String hostname;

  @Value("${spring.application.server.port}")
  Integer port;

  public static Map<String, ClientSocket> players = new HashMap<>();

  @PostConstruct
  public void init() {
  }

  public ClientSocket getSocket(String name) throws IOException {
    // if no connection yet
    if (!players.containsKey(name)) {
      logger.info("Create a new [" + name + "] socket");
      Socket s = new Socket(hostname, port);
      ClientSocket cs = new ClientSocket(s);
      players.put(name, cs);
      return cs;
    }
    logger.info("Retrieve [" + name + "]'s socket");
    return players.get(name);
  }

  public ClientSocket getOneTimeSocket() throws IOException {
    Socket s = new Socket(hostname, port);
    return new ClientSocket(s);
  }

  /**
   * Remove socket from socket map
   *
   * @param userName is the name of user to be removed
   */
  public void removeUser(String userName) throws IOException {
    players.get(userName).close();
    players.remove(userName);
    logger.info("Remove and close [" + userName + "]'s socket");
  }

}
