package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientPlayer;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.game.GameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class LobbyController {

  @Autowired
  private PlayerConnectionMapping playerMapping;

  // todo: change after user login
  private String currentUserName;
  private final ObjectMapper mapper;
  private final JSONSerializer jsonSerializer;

  public LobbyController() {
    this.jsonSerializer = new JSONSerializer();
    this.currentUserName = "test";
    this.mapper = new ObjectMapper();
  }

  @GetMapping("/lobby")
  public String enterLobby(@RequestParam(value = "name") String userName, Model model) throws IOException {
    // send getGameList request
    ClientPlayer c = playerMapping.getTemporaryPlayer();
    ObjectNode gameListReq = JsonNodeFactory.instance.objectNode();
    gameListReq.put("type", "getGameList");
    gameListReq.put("name", userName);
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
    ClientPlayer c = playerMapping.getClientPlayer("p2");
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "start");
    startReq.put("name", "p2");
    startReq.put("gameSize", size);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "game";
  }

  @GetMapping(value = "/join")
  public String join(@RequestParam(value = "id") String gameID, RedirectAttributes redirectAttributes) throws IOException {
    ClientPlayer c = playerMapping.getClientPlayer(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "join");
    startReq.put("name", currentUserName);
    startReq.put("gameID", gameID);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    if (c.recvMessage().equals(Constant.SUCCESS_NUMBER_CHOOSED)) {
      redirectAttributes.addAttribute("name", "test");
      return "redirect:game";
    }
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
    return "game";
  }
}
