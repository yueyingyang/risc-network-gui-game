package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper functions collections
 * abstract for using in different controllers and also testing...
 */
@Service
public class UtilService {
  private final JSONSerializer jsonSerializer;

  public UtilService() {
    jsonSerializer = new JSONSerializer();
  }

  /**
   * Deserialize List<ObjectNode> from JSON string
   *
   * @param mapViewString is JSON str
   * @return List<ObjectNode> could be used in js code
   * @throws JsonProcessingException
   */
  public List<ObjectNode> deNodeList(String mapViewString) throws JsonProcessingException {
    return jsonSerializer.deserializeList(mapViewString, ObjectNode.class)
            .stream().map(x -> (ObjectNode) x)
            .collect(Collectors.toList());
  }

  /**
   * Wrap terrUnit for placement phase
   *
   * @param map      is the map to be placed on
   * @param userName is the current user name, for extracting it's territory
   * @return the TerrUnitList for MVC
   */
  public TerrUnitList createTerrUnitList(GameMap map, String userName) {
    List<TerrUnit> ans = new ArrayList<>();
    for (Territory t : map.getPlayerTerritories(userName)) {
      ans.add(new TerrUnit(t.getName(), 0));
    }
    return new TerrUnitList(ans);
  }

  // Below are some mocks for local test...

  /**
   * There are many places need a map to test...
   *
   * @return a GameMap object
   */
  public GameMap createMap() {
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
    return map;
  }

  /**
   * Convert GameMap to the JSON NODE to display map
   *
   * @return the MAP display info in JSON
   */
  public List<ObjectNode> mockObjectNodes() throws JsonProcessingException {
    String emptyView = "[{\"name\":\"0\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"1\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\"},{\"name\":\"2\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"},{\"name\":\"3\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\"}]";
    String fullView = "[{\"name\":\"0\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\",\"units\":1,\"foodProd\":0,\"techProd\":0},{\"name\":\"1\",\"owner\":\"p2\",\"value\":2,\"color\":\"#97B8A3\",\"units\":2,\"foodProd\":0,\"techProd\":0},{\"name\":\"2\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\",\"units\":2,\"foodProd\":0,\"techProd\":0},{\"name\":\"3\",\"owner\":\"test\",\"value\":2,\"color\":\"#EDC3C7\",\"units\":3,\"foodProd\":0,\"techProd\":0}]";
    return deNodeList(fullView);
  }
}
