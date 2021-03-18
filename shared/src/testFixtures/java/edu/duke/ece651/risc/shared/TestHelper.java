package edu.duke.ece651.risc.shared;


import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TestHelper {
  public static ClientPlayer createClientPlayer(String serverIn, ByteArrayOutputStream serverOut, String userIn, ByteArrayOutputStream userOut) {
    return new ClientPlayer(new BufferedReader(new StringReader(serverIn)),
            new PrintWriter(serverOut, true),
            new BufferedReader(new StringReader(userIn)),
            new PrintStream(userOut));
  }

  public static GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2, "player1"),
            new PlaceEntry("1", 2, "player1"),
            new PlaceEntry("2", 2, "player2"),
            new PlaceEntry("3", 2, "player2"));
    for (ActionEntry ae : pl) {
      ae.apply(map);
    }
    return map;
  }

  public static GameMap createEasyMap(){
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 1);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 1, "player1"),
            new PlaceEntry("1", 0, "player2"));
    for (ActionEntry ae : pl) {
      ae.apply(map);
    }
    return map;
  }
}
