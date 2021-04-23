package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.web.model.ActionAjaxResBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AjaxController {
  private final PlayerSocketMap playerMapping;
  private final UtilService util;
  private final JSONSerializer serializer;

  public AjaxController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.playerMapping = playerMapping;
    this.serializer = new JSONSerializer();
  }

  /**
   * Ajax GET API for update game status (after enter / join, should wait game to start)
   *
   * @return Response with true if room is ready, else false.
   * @throws IOException if IO exception
   */
  @GetMapping(value = "/check_room_status")
  public ResponseEntity<?> checkRoomStatus() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);

    if (cs.hasNewMsg()) {
      // means the game is ready (the new msg will be the empty' game map)
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(false);
  }

  /**
   * Ajax GET API for update map after placement
   *
   * @return Response with sta tus error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/update_map")
  public ResponseEntity<?> tryUpdateMap() throws IOException {
    // REALLY NEEDED FOR UI TEST :)
//     return ResponseEntity.status(HttpStatus.ACCEPTED).body(util.mockObjectNodes());
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
      String mapViewString = cs.recvMessage();
      // 1. REJOIN: receive game over and winner info
      if (mapViewString.equals(Constant.LOSE_GAME)) {
        String continueOrOver = cs.recvMessage();
        if (continueOrOver.equals(Constant.GAME_OVER)) {
          String winnerInfo = cs.recvMessage();
          return wrapWinnerInfo(winnerInfo);
        } else {
          ObjectNode o = serializer.getOm().createObjectNode();
          o.put("lose", true);
          // rejoin but receive game_over
          return ResponseEntity.status(HttpStatus.ACCEPTED).body(o);
        }
      }
      // 2. Recv MapView
      Map<String, List<ObjectNode>> graphData = util.deNodeList(mapViewString);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(graphData);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);

  }

  /**
   * Ajax GET API for receiving updates during game watching
   *
   * @return Response with sta tus error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/watch_game")
  public ResponseEntity<ActionAjaxResBody> tryRecvWatchUpdates() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ActionAjaxResBody resBody = new ActionAjaxResBody();
    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
      String gameStatus = cs.recvMessage();
      updatedCombatOrGameOver(resBody, cs, gameStatus);
      return ResponseEntity.ok(resBody);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  /**
   * Ajax GET API for update map after placement
   *
   * @return Response with status error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/resolve_combat")
  public ResponseEntity<?> tryResolveCombat() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ActionAjaxResBody resBody = new ActionAjaxResBody();
    ClientSocket cs = playerMapping.getSocket(userName);
    if (!cs.hasNewMsg()) {
      // Continue to wait for combating result
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    // 1. recv combat result
    String combatRes = cs.recvMessage();
    resBody.setValRes((String) serializer.deserialize(combatRes, String.class));
    // 2. recv LOSE / CONTINUE
    String playerStatus = cs.recvMessage();
    // 2.1 CONTINUE
    if (playerStatus.equals(Constant.CONTINUE_PLAYING)) {
      handleContinue(resBody, cs);
    } else {
    // 2.2 LOSE
      handleLose(resBody, cs);
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(resBody);
  }

  @PostMapping(value = "/choose_watch")
  public ResponseEntity<?> chooseToWatch() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    // Send to server
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(Constant.WATCH_GAME);
    Map<String, List<ObjectNode>> graphData = util.deNodeList(cs.recvMessage());
    return ResponseEntity.ok().body(graphData);
  }

  /**
   * Used in watch_game
   *
   * @param resBody    is the body of response
   * @param cs         is the client socket to recv updates
   * @param gameStatus is the flag if it's GAME OVER or Combat Result
   * @throws IOException
   */
  private void updatedCombatOrGameOver(ActionAjaxResBody resBody, ClientSocket cs, String gameStatus) throws IOException {
    // 1. Game over and send back winner info
    // 2. Send back resolve combat and updated map
    resBody.setValRes((String) serializer.deserialize(gameStatus, String.class));
    String mapViewString = cs.recvMessage();
    if (mapViewString.equals(Constant.GAME_OVER)) {
      String winnerInfo = cs.recvMessage();
      resBody.setWinnerInfo(winnerInfo);
    } else {
      Map<String, List<ObjectNode>> graphData = util.deNodeList(mapViewString);
      resBody.setGraphData(graphData);
    }
  }

  // Wrap winner info as a JSON node
  private ResponseEntity<?> wrapWinnerInfo(String winnerInfo) {
    ObjectNode o = serializer.getOm().createObjectNode();
    o.put("winnerInfo", winnerInfo);
    // rejoin but receive game_over
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(o);
  }


  private void handleLose(ActionAjaxResBody resBody, ClientSocket cs) throws IOException {
    String gameStatus = cs.recvMessage(); // GAME_OVER or Continue_playing
    if (gameStatus.equals(Constant.GAME_OVER)) {
      // 2.2.1 Game over
      String winnerInfo = cs.recvMessage();
      resBody.setWinnerInfo(winnerInfo);
    }
    resBody.setGraphData(null);
    resBody.setWin(false);
  }

  private void handleContinue(ActionAjaxResBody resBody, ClientSocket cs) throws IOException {
    String gameStatus = cs.recvMessage(); // GAME_OVER or next turn's map
    if (!gameStatus.equals(Constant.GAME_OVER)) {
      // 2.1.2 Next turn starts!
      resBody.setGraphData(util.deNodeList(gameStatus));
    } else {
      // 2.1.1 You are the last player! You're the winner!
      resBody.setGraphData(null);
      resBody.setWin(true);
    }
  }
}
