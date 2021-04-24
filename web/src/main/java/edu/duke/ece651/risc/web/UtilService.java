package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
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
  public Map<String, List<ObjectNode>> deNodeList(String mapViewString) throws JsonProcessingException {
    TypeReference<HashMap<String, List<ObjectNode>>> typeRef
            = new TypeReference<>() {
    };
    Map<String, List<ObjectNode>> deView = jsonSerializer.getOm().readValue(mapViewString, typeRef);
    return deView;
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
}
