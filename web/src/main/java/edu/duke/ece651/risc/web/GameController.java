package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/game")
public class GameController {
  private final PlayerSocketMap playerMapping;
  private final UtilService util;

  // todo: change after user login
  private String currentUserName;

  // utils
  private final JSONSerializer jsonSerializer;

  Logger logger = LoggerFactory.getLogger(GameController.class);

  public GameController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.jsonSerializer = new JSONSerializer();
    this.currentUserName = "test";
    this.playerMapping = playerMapping;
  }

  /**
   * The unit placement page
   *
   * @param model is the model to add attribute for view to display
   * @return is the name of view template html
   * @throws IOException
   */
  @GetMapping("/place")
  public String place(Model model) throws IOException {
    ClientSocket cs = playerMapping.getSocket(currentUserName);
    GameMap map = (GameMap) jsonSerializer.deserialize(cs.recvMessage(), GameMap.class);
    int totalUnits = Integer.parseInt(cs.recvMessage());
    String mapViewString = cs.recvMessage();
    List<ObjectNode> graphData = util.deNodeList(mapViewString);
//    Below 2 lines are for local test
//    GameMap map = util.createMap();
//    int totalUnits = 6;
//    List<ObjectNode> graphData = util.mockObjectNodes();
    model.addAttribute("graphData", graphData);
    model.addAttribute("wrapper", util.createTerrUnitList(map, currentUserName));
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
    playerMapping.getSocket(currentUserName).sendMessage(json);
    return "redirect:play";
  }

  /**
   * Game play page, render the stored map
   *
   * @return game
   */
  @GetMapping(value = "/play")
  public String playOneTurn(Model model) {
    model.addAttribute("action", new AttackEntry("", "", 0, currentUserName));
    return "game";
  }

  /**
   * todo: need to be moved to AjaxController after refactoring with V2MapView
   * Ajax GET API for update map after placement
   *
   * @return Response with status error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/update_map")
  public @ResponseBody
  ResponseEntity<?> tryUpdateMap() throws IOException {
//    Below 2 lines are for local test
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(mockObjectNodes());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    ClientSocket cs = playerMapping.getSocket(currentUserName);
    if (cs.hasNewMsg()) {
      String mapViewString = cs.recvMessage();
      List<ObjectNode> graphData = util.deNodeList(mapViewString);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(graphData);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
}