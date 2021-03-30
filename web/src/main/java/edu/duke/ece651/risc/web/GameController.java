package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

@Controller
public class GameController {
  @Autowired
  private PlayerSocketMap playerMapping;

  // todo: change after user login
  private String currentUserName;
  // need update after switch rooms
  private List<String> players;

  // utils
  private final ObjectMapper mapper;
  private final JSONSerializer jsonSerializer;
  private final List<String> colorPalette;
  Logger logger = LoggerFactory.getLogger(GameController.class);

  public GameController() {
    this.jsonSerializer = new JSONSerializer();
    this.currentUserName = "p2";
    this.mapper = new ObjectMapper();
    this.colorPalette = Arrays.asList("#97B8A3", "#EDC3C7", "#FDF06F", "#A6CFE2", "#9C9CDD");
    this.players = Arrays.asList("p2", "test");
  }

  /**
   * The unit placement page
   *
   * @param model is the model to add attribute for view to display
   * @return is the name of view template html
   * @throws IOException
   */
  @GetMapping("/game/place")
  public String place(Model model) throws IOException {
    GameMap map = (GameMap) jsonSerializer.deserialize(playerMapping.getSocket(currentUserName).recvMessage(), GameMap.class);
    int totalUnits = Integer.parseInt(playerMapping.getSocket(currentUserName).recvMessage());
//    Below 2 lines are for local test
//    GameMap map = createMap();
//    int totalUnits = 6;
    List<ObjectNode> graphData = getObjectNodes(map, players);
    model.addAttribute("graphData", graphData);
    model.addAttribute("wrapper", createTerrUnitList(map, currentUserName));
    model.addAttribute("units", totalUnits);
    model.addAttribute("message", null);
    return "placement";
  }

  /**
   * Placement submission API
   *
   * @param list is the TerrUnitList user input
   * @return redirect to game page
   * @throws IOException if recv/send exception
   */
  @PostMapping(value = "/submit_place")
  public String place(@ModelAttribute(value = "wrapper") TerrUnitList list) throws IOException {
    List<ActionEntry> placementList = new ArrayList<>();
    for (TerrUnit tu : list.getTerrUnitList()) {
      placementList.add(new PlaceEntry(tu.getTerrName(), tu.getUnit(), currentUserName));
    }
    String json = jsonSerializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(placementList);
    logger.info(json);
    playerMapping.getSocket(currentUserName).sendMessage(json);
    return "redirect:game/play";
  }

  /**
   * Game play page, render the stored map
   *
   * @return game
   */
  @GetMapping(value = "/game/play")
  public String playOneTurn(Model model) {
    // todo: should refactor createMap() to retrieve stored game map
    List<ObjectNode> graphData = getObjectNodes(createMap(), players);
    model.addAttribute("graphData", graphData);
    return "game";
  }

  /**
   * Ajax GET API for update map after placement
   *
   * @return Response with status error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/update_map")
  public @ResponseBody
  ResponseEntity<?> tryUpdateMap() throws IOException {
//    Below 2 lines are for local test
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(getObjectNodes(createMap(), players));
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    ClientSocket cs = playerMapping.getSocket(currentUserName);
    if (cs.hasNewMsg()) {
      GameMap map = (GameMap) jsonSerializer.deserialize(cs.recvMessage(), GameMap.class);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(getObjectNodes(map, players));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * Ajax GET API for update game status (after enter / join, should wait game to start)
   *
   * @return Response with true if room is ready, else false.
   * @throws IOException if IO exception
   */
  @GetMapping(value = "/check_room_status")
  public @ResponseBody
  ResponseEntity<?> checkRoomStatus() throws IOException {
//    Below 1 lines are for local test
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
    ClientSocket cs = playerMapping.getSocket(currentUserName);
    if (cs.hasNewMsg()) {
      // means the game is ready (the new msg will be the empty' game map)
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(false);
  }

  // Below are helper functions

  /**
   * Convert GameMap to the JSON NODE to display map
   *
   * @param map     is map to be converted
   * @param players is the player list to do the color mapping
   * @return the MAP display info in JSON
   */
  private List<ObjectNode> getObjectNodes(GameMap map, List<String> players) {
    Map<String, String> colorMapping = new HashMap<>();
    for (int i = 0; i < players.size(); i++) {
      colorMapping.put(players.get(i), colorPalette.get(i));
    }
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

  /**
   * Wrap terrUnit for placement phase
   *
   * @param map      is the map to be placed on
   * @param userName is the current user name, for extracting it's territory
   * @return the TerrUnitList for MVC
   */
  protected TerrUnitList createTerrUnitList(GameMap map, String userName) {
    List<TerrUnit> ans = new ArrayList<>();
    for (Territory t : map.getPlayerTerritories(userName)) {
      ans.add(new TerrUnit(t.getName(), 0));
    }
    return new TerrUnitList(ans);
  }


  /**
   * There are many places need a map to test...
   *
   * @return a GameMap object
   */
  private GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    Collections.shuffle(players);
    return v1f.createMap(players, 2);
  }

  /**
   * For update player list after switching game room
   *
   * @param players is the updated player list
   */
  public void setPlayers(List<String> players) {
    this.players = players;
  }
}