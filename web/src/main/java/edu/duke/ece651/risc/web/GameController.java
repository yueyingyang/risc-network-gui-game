package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import edu.duke.ece651.risc.shared.game.TerrUnit;
import edu.duke.ece651.risc.shared.game.TerrUnitList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game")
public class GameController {
  private final PlayerSocketMap playerMapping;
  private final UtilService util;

  // utils
  private final JSONSerializer jsonSerializer;

  Logger logger = LoggerFactory.getLogger(GameController.class);

  public GameController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.jsonSerializer = new JSONSerializer();
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

    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    GameMap map = (GameMap) jsonSerializer.deserialize(cs.recvMessage(), GameMap.class);
    int totalUnits = Integer.parseInt(cs.recvMessage());
    String mapViewString = cs.recvMessage();
    Map<String, List<ObjectNode>> graphData = util.deNodeList(mapViewString);
    model.addAttribute("graphData", graphData);
    model.addAttribute("wrapper", util.createTerrUnitList(map, userName));
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
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    List<ActionEntry> placementList = new ArrayList<>();
    for (TerrUnit tu : list.getTerrUnitList()) {
      placementList.add(new PlaceEntry(tu.getTerrName(), tu.getUnit(), userName));
    }
    String json = jsonSerializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(placementList);
    cs.sendMessage(json);
    String valRes = cs.recvMessage();
    if (valRes.equals("invalid")) {
      return "redirect:place";
    }
    return "redirect:play";
  }

  /**
   * Game play page, render the stored map
   *
   * @return game
   */
  @GetMapping(value = "/play")
  public String playOneTurn(Model model) {
    model.addAttribute("soldierType", Constant.soldierCost.keySet().stream().sorted().collect(Collectors.toList()));
    return "game";
  }

}