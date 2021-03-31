package edu.duke.ece651.risc.shared.game;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.beans.ConstructorProperties;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GameInfo {
  public final Integer id;
  public final List<String> players;

  @ConstructorProperties({"id", "players"})
  public GameInfo(Integer id, List<String> players) {
    this.id = id;
    this.players = players;
  }
}
