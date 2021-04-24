package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.V1MapFactory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UtilServiceTest {
  @Test
  public void test_deNode_list() throws JsonProcessingException {
    UtilService us = new UtilService();
    String fullView = "{\"data\":[{\"name\":\"0\",\"owner\":\"user3\",\"value\":5,\"color\":\"#97B8A3\",\"foodProd\":20,\"techProd\":20,\"unit0\":1334163,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":230,\"y\":200},{\"name\":\"1\",\"owner\":\"user3\",\"value\":5,\"color\":\"#97B8A3\",\"foodProd\":20,\"techProd\":20,\"unit0\":1334163,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":200,\"y\":230},{\"name\":\"2\",\"owner\":\"user2\",\"value\":5,\"color\":\"#EDC3C7\",\"foodProd\":20,\"techProd\":20,\"unit0\":1334163,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":170,\"y\":200},{\"name\":\"3\",\"owner\":\"user2\",\"value\":5,\"color\":\"#EDC3C7\",\"foodProd\":20,\"techProd\":20,\"unit0\":1334163,\"unit1\":0,\"unit2\":0,\"unit3\":0,\"unit4\":0,\"unit5\":0,\"unit6\":0,\"x\":200,\"y\":170}],\"playerInfo\":[{\"name\":\"user2\",\"techLevel\":1,\"foodRes\":53366560,\"techRes\":53366560,\"isRequested\":false}],\"links\":[{\"source\":\"0\",\"target\":\"1\"},{\"source\":\"0\",\"target\":\"2\"},{\"source\":\"0\",\"target\":\"3\"},{\"source\":\"1\",\"target\":\"0\"},{\"source\":\"1\",\"target\":\"2\"},{\"source\":\"2\",\"target\":\"0\"},{\"source\":\"2\",\"target\":\"1\"},{\"source\":\"2\",\"target\":\"3\"},{\"source\":\"3\",\"target\":\"0\"},{\"source\":\"3\",\"target\":\"2\"}]}";
    Map<String, List<ObjectNode>> map = us.deNodeList(fullView);
    assertTrue(map.containsKey("data"));
    assertEquals(4, map.get("data").size());
  }

  @Test
  public void test_create_terr_unit_list() {
    V1MapFactory v1f = new V1MapFactory();
//    Collections.shuffle(players);
    GameMap map = v1f.createMap(Arrays.asList("p2", "test"), 2);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2, "p2"),
            new PlaceEntry("1", 2, "p2"),
            new PlaceEntry("2", 2, "test"),
            new PlaceEntry("3", 2, "test"));
    for (ActionEntry ae : pl) {
      ae.apply(map, null);
    }
    UtilService us = new UtilService();
    assertEquals(2,
            us.createTerrUnitList(map, "p2").getTerrUnitList().size());
  }
}