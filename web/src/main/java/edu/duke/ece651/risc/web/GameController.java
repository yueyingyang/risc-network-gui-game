package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.GameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
public class GameController {
  @Autowired
  private PlayerConnectionMapping playerMapping;

  // todo: change after user login
  private String currentUserName;
  private final ObjectMapper mapper;
  private final JSONSerializer jsonSerializer;

  public GameController() {
    this.jsonSerializer = new JSONSerializer();
    this.currentUserName = "test";
    this.mapper = new ObjectMapper();
  }

  @GetMapping("/game")
  public String greeting(@RequestParam(value = "name") String userName, Model model) throws IOException {

    List<String> colorPalette = Arrays.asList("#97B8A3", "#EDC3C7", "#FDF06F", "#A6CFE2", "#9C9CDD");
    List<String> players = Arrays.asList("p2", "test");
    Map<String, String> colorMapping = new HashMap<>();
    for (int i = 0; i < players.size(); i++) {
      colorMapping.put(players.get(i), colorPalette.get(i));
    }
//    GameMap map = (GameMap) jsonSerializer.deserialize(playerMapping.getClientPlayer(userName).recvMessage(), GameMap.class);
//    int totalUnits = Integer.parseInt(playerMapping.getClientPlayer(userName).recvMessage());
    GameMap map = createMap();
    int totalUnits = 6;

    List<ObjectNode> graphData = getObjectNodes(colorMapping, map);
    model.addAttribute("graphData", graphData);
    model.addAttribute("territories", getTerritoryNameList(map, userName));
    model.addAttribute("units", totalUnits);
    return "game";
  }

  @PostMapping(value = "/place")
  public String start(@RequestBody String placement) throws IOException {
    List<ActionEntry> placementList = new ArrayList<>();
    System.out.println(placement);
//    placements.add()
//    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
//    placementList.add(new PlaceEntry(t.getName(), num, getName()));

//    playerMapping.getClientPlayer("test").sendMessage(jsonSerializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
//    }).writeValueAsString(placementList));
    return "game";
  }

  /**
   * Parse GameMap as JSON Node for Map display
   * source:[
   * {"owner":"Red", "resources":"10", "value":"2"},
   * {"owner":"Red", "resources":"10", "value":"2"},
   * ]
   *
   * @param colorMapping
   * @param map
   * @return
   */
  private List<ObjectNode> getObjectNodes(Map<String, String> colorMapping, GameMap map) {
    List<ObjectNode> graphData = new ArrayList<>();
    for (Territory t : map.getAllTerritories()) {
      ObjectNode o = mapper.createObjectNode();
      o.put("name", t.getName());
      o.put("owner", t.getOwnerName());
      o.put("resources", 10);
      o.put("value", 2);
      o.put("color", colorMapping.get(t.getOwnerName()));
      graphData.add(o);
    }
    return graphData;
  }


  //  mock a map
  private GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    List<String> players = Arrays.asList("test", "p2");
    GameMap map = v1f.createMap(players, 2);
//    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2, "test"),
//            new PlaceEntry("1", 2, "test"),
//            new PlaceEntry("2", 2, "player2"),
//            new PlaceEntry("3", 2, "player2"));
//    for (ActionEntry ae : pl) {
//      ae.apply(map);
//    }
    return map;
  }

  private List<String> getTerritoryNameList(GameMap map, String userName) {
    List<String> ans = new ArrayList<>();
    for (Territory t : map.getPlayerTerritories(userName)) {
      ans.add(t.getName());
    }
    return ans;
  }
}