package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.*;
import edu.duke.ece651.risc.web.model.ActionAjaxResBody;
import edu.duke.ece651.risc.web.model.UserActionInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
//    Below 1 lines are for local test
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(false);

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
//    Below 2 lines are for local test
    //   return ResponseEntity.status(HttpStatus.ACCEPTED).body(util.mockObjectNodes());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
      String mapViewString = cs.recvMessage();
      // 1. REJOIN: receive game over and winner info
      if (mapViewString.equals(Constant.GAME_OVER)) {
        String winnerInfo = cs.recvMessage();
        return wrapWinnerInfo(winnerInfo);
      } else if (mapViewString.equals(Constant.LOSE_GAME)) {
        ObjectNode o = serializer.getOm().createObjectNode();
        o.put("lose", true);
//        rejoin but receive game_over
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(o);
      }
      // 2. Recv MapView
      List<ObjectNode> graphData = util.deNodeList(mapViewString);
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
   * Attack action validation
   *
   * @param input is serialized form
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  @PostMapping(value = "/attack", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> attack(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//    Wrap a Attack entry
    FancyAttackEntry attackEntry = new FancyAttackEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            input.getFromType(),
            userName);
    System.out.println(serializer.serialize(attackEntry));
    return getActionAjaxResBodyResponseEntity(attackEntry);
  }

  /**
   * Move action validation
   *
   * @param input is serialized form
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> move(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//    Wrap a Move entry
    FancyMoveEntry moveEntry = new FancyMoveEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            input.getFromType(),
            userName);
    System.out.println(serializer.serialize(moveEntry));
    return getActionAjaxResBodyResponseEntity(moveEntry);
  }

  /**
   * Update soldier action validation
   *
   * @param input is serialized form
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  @PostMapping(value = "/soldier", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> soldier(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//    Wrap a Soldier entry
    SoldierEntry soldierEntry = new SoldierEntry(
            input.getToName(),
            input.getFromType(),
            input.getToType(),
            input.getSoldierNum(),
            userName);
    System.out.println(serializer.serialize(soldierEntry));
    return getActionAjaxResBodyResponseEntity(soldierEntry);
  }

  /**
   * Update technology action validation
   *
   * @param input is serialized form
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  @PostMapping(value = "/tech", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> tech(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//    Wrap a Tech entry
    TechEntry techEntry = new TechEntry(
            userName);
    System.out.println(serializer.serialize(techEntry));
    return getActionAjaxResBodyResponseEntity(techEntry);
  }

  /**
   * Commit button will send COMMIT to server
   *
   * @return nothing but ok status
   * @throws IOException
   */
  @PostMapping(value = "/commit")
  public ResponseEntity<?> commit() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//    local test
//    return ResponseEntity.ok(null);
//    Wrap a commit request
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(Constant.ORDER_COMMIT);
    return ResponseEntity.ok(null);
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
//    for local test
//    resBody.setValRes("test resolve combat result");
//    resBody.setGraphData(util.mockObjectNodes());
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(resBody);
    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
//  1. recv combat result
      String combatRes = cs.recvMessage();
      resBody.setValRes((String) serializer.deserialize(combatRes, String.class));
//  2. recv LOSE / CONTINUE
      String playerStatus = cs.recvMessage();
//      2.1 CONTINUE
      if (playerStatus.equals(Constant.CONTINUE_PLAYING)) {
        String gameStatus = cs.recvMessage(); // GAME_OVER or next turn's map
        if (!gameStatus.equals(Constant.GAME_OVER)) {
//          2.1.2 Next turn starts!
          resBody.setGraphData(util.deNodeList(gameStatus));
        } else {
//          2.1.1 You are the last player! You're the winner!
          resBody.setGraphData(null);
          resBody.setWin(true);
        }
      } else {
//        2.2 LOSE
        String gameStatus = cs.recvMessage(); // GAME_OVER or next turn's map
        if (gameStatus.equals(Constant.GAME_OVER)) {
//          2.2.1 Game over
          String winnerInfo = cs.recvMessage();
          resBody.setWinnerInfo(winnerInfo);
        }
        resBody.setGraphData(null);
        resBody.setWin(false);
      }
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(resBody);
    }
//    Continue to wait for combating result
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  @PostMapping(value = "/choose_watch")
  public ResponseEntity<?> chooseToWatch() throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    //    Send to server
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(Constant.WATCH_GAME);
    return ResponseEntity.ok(null);
  }


  /**
   * Helper function for validating action with socket server
   *
   * @param ae is the action entry
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  private ResponseEntity<ActionAjaxResBody> getActionAjaxResBodyResponseEntity(ActionEntry ae) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    //    Send to server
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(serializer.serialize(ae));
//    Receive validation res if valid then append msg to msg box,
    String validRes = cs.recvMessage();
//    if not append error info to msg box
//    Receive new map view
    List<ObjectNode> graphData = util.deNodeList(cs.recvMessage());

//    Below 2 lines for mock recv() for local test
//    List<ObjectNode> graphData = util.mockObjectNodes();
//    String validRes = "test validRes";

    ActionAjaxResBody resBody = new ActionAjaxResBody();
    resBody.setGraphData(graphData);
    resBody.setValRes(validRes);
    return ResponseEntity.ok(resBody);
  }


  // Wrap winner info as a JSON node
  private ResponseEntity<?> wrapWinnerInfo(String winnerInfo) {
    ObjectNode o = serializer.getOm().createObjectNode();
    o.put("winnerInfo", winnerInfo);
//        rejoin but receive game_over
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(o);
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
    if (gameStatus.equals(Constant.GAME_OVER)) {
      String winnerInfo = cs.recvMessage();
      resBody.setWinnerInfo(winnerInfo);
    } else {
      // 2. Send back resolve combat and updated map
      resBody.setValRes((String) serializer.deserialize(gameStatus, String.class));
      String mapViewString = cs.recvMessage();
      List<ObjectNode> graphData = util.deNodeList(mapViewString);
      resBody.setGraphData(graphData);
    }
  }

}
