package edu.duke.ece651.risc.shared.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static edu.duke.ece651.risc.shared.Constant.COLORS;
import static org.junit.jupiter.api.Assertions.*;
import static edu.duke.ece651.risc.shared.MapViewTest.getGameMap;

class V2MapViewTest {
  @Test
  void test_to_string() throws JsonProcessingException {
    List<String> playerNames = Arrays.asList("John", "Tom");
    GameMap map = getGameMap(1, playerNames);
    Army army = new Army("John", 5, "3");
    Territory t = map.getTerritory("0");
    t.setMyArmy(army);
    Map<String, String> colorMapping = new HashMap<>();
    colorMapping.put("John", "#97B8A3");
    colorMapping.put("Tom", "#EDC3C7");
    V2MapView view = new V2MapView(map, playerList(playerNames), new PlayerInfo("John", 1, 20, 20), colorMapping);
    String fullMapView = view.toString(true);
    JSONSerializer s = new JSONSerializer();
    TypeReference<HashMap<String, List<ObjectNode>>> typeRef
            = new TypeReference<>() {
    };
    Map<String, List<ObjectNode>> deView = s.getOm().readValue(fullMapView, typeRef);
    assertEquals("[{\"name\":\"0\",\"value\":5,\"owner\":\"John\",\"color\":\"#97B8A3\",\"foodProd\":20,\"techProd\":20,\"unit0\":0,\"unit1\":0,\"unit2\":0,\"unit3\":5,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":230,\"y\":200}, {\"name\":\"1\",\"value\":5,\"owner\":\"Tom\",\"color\":\"#EDC3C7\",\"foodProd\":20,\"techProd\":20,\"unit0\":1,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":170,\"y\":200}]", deView.get("data").toString());
    assertEquals("[{\"source\":\"0\",\"target\":\"1\"}, {\"source\":\"1\",\"target\":\"0\"}]", deView.get("links").toString());
    assertEquals("[{\"name\":\"John\",\"techLevel\":1,\"foodRes\":20,\"techRes\":20,\"isRequested\":false}]", deView.get("playerInfo").toString());
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