package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class GameController {
  @Autowired
  private PlayerSocketMap playerMapping;

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
    model.addAttribute("wrapper", createTerriUnitList(map, userName));
    model.addAttribute("units", totalUnits);
    return "game";
  }

  @PostMapping(value = "/place")
  public String place(@ModelAttribute(value = "wrapper") TerrUnitList list, Model model) throws IOException {
    List<ActionEntry> placementList = new ArrayList<>();
    for(TerrUnit tu : list.getTerrUnitList()){
      System.out.println(tu.getTerrName());
      System.out.println(tu.getUnit());
    }

//    placements.add()
//    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
//    placementList.add(new PlaceEntry(t.getName(), num, getName()));

//    playerMapping.getClientPlayer("test").sendMessage(jsonSerializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
//    }).writeValueAsString(placementList));
    model.addAttribute("wrapper", list);
    return "game";
  }

//  @GetMapping(value = "/update_map")
//  public @ResponseBody
//  List<ObjectNode> tryUpdateMap() {
//    if(playerMapping.getClientPlayer().getInputStream())
//  }

  // Below is helper functions

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

  protected TerrUnitList createTerriUnitList(GameMap map, String userName) {
    List<TerrUnit> ans = new ArrayList<>();
    for (Territory t : map.getPlayerTerritories(userName)) {
      ans.add(new TerrUnit(t.getName(), 0));
    }
    return new TerrUnitList(ans);
  }
}