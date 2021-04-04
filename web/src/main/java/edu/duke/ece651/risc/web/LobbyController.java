package edu.duke.ece651.risc.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.game.GameInfo;

@Controller
public class LobbyController {
  private final PlayerSocketMap playerMapping;
  private final ObjectMapper mapper;
  private final JSONSerializer jsonSerializer;

  public LobbyController(PlayerSocketMap playerMapping) {
    this.jsonSerializer = new JSONSerializer();
    //this.currentUserName = "test";
    this.mapper = new ObjectMapper();
    this.playerMapping = playerMapping;
  }

  /**
   * Lobby page: request game lists from server
   *
   * @param model    is object be sent to html template
   * @return lobby.html
   * @throws IOException if recv/send exception
   */
  @GetMapping("/lobby")
  public String enterLobby(Model model) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    // send getGameList request: wrap a JSON request as defined in doc
    ClientSocket c = playerMapping.getOneTimeSocket();
    ObjectNode gameListReq = JsonNodeFactory.instance.objectNode();
    gameListReq.put("type", "getGameList");
    gameListReq.put("name", userName);
    c.sendMessage(mapper.writeValueAsString(gameListReq));
    // recv 2 times for 2 type of list: all open games and all joined games
    String allOpen = c.recvMessage();
    String allJoined = c.recvMessage();
    List<GameInfo> allOpenGames = (List<GameInfo>) jsonSerializer.getOm().readValue(allOpen, new TypeReference<Collection<GameInfo>>() {
    });
    List<GameInfo> allJoinedGames = (List<GameInfo>) jsonSerializer.getOm().readValue(allJoined, new TypeReference<Collection<GameInfo>>() {
    });
    // add the models into template
    model.addAttribute("allJoinedGames", allJoinedGames);
    model.addAttribute("allOpenGames", allOpenGames);
    return "lobby";
  }

  /**
   * Wrap input game size as a JSON request, send to socket server
   *
   * @param size is the user input game size
   * @return redirect to waiting room, waiting for game to start
   * @throws IOException if IO exception
   */
  @PostMapping(value = "/start")
  public String start(@RequestParam(name = "size") String size) throws IOException {
    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket c = playerMapping.getSocket(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "start");
    startReq.put("name", currentUserName);
    startReq.put("gameSize", size);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "redirect:waiting";
  }

  /**
   * Wrap gameID as a JSON request, send to socket server
   *
   * @param gameID is the selected game
   * @return redirect to waiting room
   * @throws IOException if IO exception
   */
  @GetMapping(value = "/join")
  public String join(@RequestParam(value = "id") String gameID) throws IOException {
    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket c = playerMapping.getSocket(currentUserName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "join");
    startReq.put("name", currentUserName);
    startReq.put("gameID", gameID);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    if (c.recvMessage().equals(Constant.SUCCESS_NUMBER_CHOOSED)) {
      return "redirect:waiting";
    }
    return "lobby";
  }

  /**
   * Enter waiting room after START or JOIN
   *
   * @return waiting_room html
   */
  @GetMapping(value = "/waiting")
  public String enterWaitingRoom() {
    return "waiting_room";
  }


  @GetMapping(value = "/rejoin")
  public String rejoin(@RequestParam(name = "gameId") String gameId) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket c = playerMapping.getSocket(userName);
    ObjectNode startReq = JsonNodeFactory.instance.objectNode();
    startReq.put("type", "rejoin");
    startReq.put("name", userName);
    startReq.put("gameID", gameId);
    c.sendMessage(new ObjectMapper().writeValueAsString(startReq));
    return "redirect:/game/play";
  }
}





