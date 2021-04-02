package edu.duke.ece651.risc.shared.game;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.ServerPlayer;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.duke.ece651.risc.shared.Constant.COLORS;
import static org.junit.jupiter.api.Assertions.*;
import static edu.duke.ece651.risc.shared.MapViewTest.getGameMap;

class V2MapViewTest {
  @Test
  void test_to_string() {
    List<String> playerNames = Arrays.asList("John", "Tom");
    GameMap map = getGameMap(1, playerNames);
    V2MapView view = new V2MapView(map, playerList(playerNames));
    assertEquals("[{\"name\":\"0\",\"owner\":\"John\",\"value\":2,\"color\":\"#97B8A3\",\"units\":1,\"foodProd\":0,\"techProd\":0},{\"name\":\"1\",\"owner\":\"Tom\",\"value\":2,\"color\":\"#EDC3C7\",\"units\":1,\"foodProd\":0,\"techProd\":0}]",
            view.toString(true));
    assertEquals("[{\"name\":\"0\",\"owner\":\"John\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"1\",\"owner\":\"Tom\",\"value\":2,\"color\":\"#EDC3C7\"}]", view.toString(false));
  }

  List<ServerPlayer> playerList(List<String> names) {
    List<ServerPlayer> sp = new ArrayList<>();
    for (String n : names) {
      ServerPlayer s = new ServerPlayer(null, null, null);
      s.setName(n);
      sp.add(s);
      s.setColor(Color.decode(COLORS[sp.size() - 1]));
    }
    return sp;
  }
}