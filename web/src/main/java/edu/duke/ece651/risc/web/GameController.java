package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.game.GameInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Controller
public class GameController {
  private final PlayerConnectionMapping playerMapping;

  // todo: change after user login
  private String currentUserName;
  private final ObjectMapper mapper;
  private final JSONSerializer jsonSerializer;

  public GameController(PlayerConnectionMapping playerMapping) {
    this.jsonSerializer = new JSONSerializer();
    this.currentUserName = "test";
    this.playerMapping = playerMapping;
    this.mapper = new ObjectMapper();
  }

  @GetMapping("/game")
  public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
    V1MapFactory v1f = new V1MapFactory();
    List<String> players = Arrays.asList("player1", "player2");
    GameMap map = v1f.createMap(players, 2);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2, "player1"),
            new PlaceEntry("1", 2, "player1"),
            new PlaceEntry("2", 2, "player2"),
            new PlaceEntry("3", 2, "player2"));
    for (ActionEntry ae : pl) {
      ae.apply(map);
    }

    List<String> colorPalette = Arrays.asList("#97B8A3", "#EDC3C7", "#FDF06F", "#A6CFE2", "#9C9CDD");
    Map<String, String> colorMapping = new HashMap<>();
    for (int i = 0; i < players.size(); i++) {
      colorMapping.put(players.get(i), colorPalette.get(i));
    }
    //    source:[
    //    {"owner":"Red", "resources":"10", "value":"2"},
    //    {"owner":"Red", "resources":"10", "value":"2"},
    //    ]
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

    model.addAttribute("graphData", graphData);
    return "game";
  }

  @GetMapping("/lobby")
  public String enterLobby(Model model) throws IOException {
    // send getGameList request
    ClientPlayer c = playerMapping.getTemporaryPlayer();
    ObjectNode gameListReq = JsonNodeFactory.instance.objectNode();
    gameListReq.put("type", "getGameList");
    gameListReq.put("name", currentUserName);
    c.sendMessage(mapper.writeValueAsString(gameListReq));
    // recv 2 times for 2 type of list
    String allOpen = c.recvMessage();
    String allJoined = c.recvMessage();
    List<GameInfo> allOpenGames = (List<GameInfo>) jsonSerializer.getOm().readValue(allOpen, new TypeReference<Collection<GameInfo>>() {
    });
    List<GameInfo> allJoinedGames = (List<GameInfo>) jsonSerializer.getOm().readValue(allJoined, new TypeReference<Collection<GameInfo>>() {
    });
    model.addAttribute("allJoinedGames", allJoinedGames);
    model.addAttribute("allOpenGames", allOpenGames);
    return "lobby";
  }


  @PostMapping(value = "/start")
  public String start(@RequestParam(name = "size") String size) throws IOException {
    ClientPlayer c = playerMapping.getClientPlayer(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "start");
    startReq.put("name", currentUserName);
    startReq.put("gameSize", size);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "lobby";
  }

  @GetMapping(value = "/join")
  public String join(@RequestParam(value = "id") String gameID) throws IOException {
    ClientPlayer c = playerMapping.getClientPlayer(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "join");
    startReq.put("name", currentUserName);
    startReq.put("gameID", gameID);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "lobby";
  }

  @PostMapping(value = "/rejoin")
  public String rejoin(@RequestParam(name = "gameID") String gameID) throws IOException {
    ClientPlayer c = playerMapping.getClientPlayer(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "rejoin");
    startReq.put("name", currentUserName);
    startReq.put("gameID", gameID);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "lobby";
  }
}