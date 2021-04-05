package edu.duke.ece651.risc.shared.game;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.entry.SoldierEntry;
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
    Army army = new BasicArmy("John", 5, "3");
    Territory t = map.getTerritory("0");
    t.setMyArmy(army);
    V2MapView view = new V2MapView(map, playerList(playerNames));
    assertEquals("[{\"name\":\"0\",\"owner\":\"John\",\"value\":5,\"color\":\"#97B8A3\",\"foodProd\":20,\"techProd\":20,\"unit0\":0,\"unit1\":0,\"unit2\":0,\"unit3\":5,\"unit4\":0,\"unit5\":0,\"unit6\":0},{\"name\":\"1\",\"owner\":\"Tom\",\"value\":5,\"color\":\"#EDC3C7\",\"foodProd\":20,\"techProd\":20,\"unit0\":1,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0}]",
            view.toString(true));
    assertEquals("[{\"name\":\"0\",\"owner\":\"John\",\"value\":5,\"color\":\"#97B8A3\"},{\"name\":\"1\",\"owner\":\"Tom\",\"value\":5,\"color\":\"#EDC3C7\"}]", view.toString(false));
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