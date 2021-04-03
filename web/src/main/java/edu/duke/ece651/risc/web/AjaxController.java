package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.AttackEntry;
import edu.duke.ece651.risc.shared.entry.MoveEntry;
import edu.duke.ece651.risc.web.model.ActionAjaxResBody;
import edu.duke.ece651.risc.web.model.UserActionInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class AjaxController {

  private final PlayerSocketMap playerMapping;
  private final UtilService util;
  private final JSONSerializer serializer;

  // todo: change after user login
  private String userName;


  public AjaxController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.userName = "test";
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
   * @return Response with status error or success, if success then body is updated GAMEMAP
   * @throws IOException
   */
  @GetMapping(value = "/update_map")
  public ResponseEntity<?> tryUpdateMap() throws IOException {
//    Below 2 lines are for local test
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(util.mockObjectNodes());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
      String mapViewString = cs.recvMessage();
      List<ObjectNode> graphData = util.deNodeList(mapViewString);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(graphData);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }


  @PostMapping(value = "/attack", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> attack(@RequestBody UserActionInput input) throws IOException {
//    Wrap a Attack entry
    AttackEntry attackEntry = new AttackEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            userName);
    System.out.println(serializer.serialize(attackEntry));
    return getActionAjaxResBodyResponseEntity(attackEntry);
  }

  @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> move(@RequestBody UserActionInput input) throws IOException {
//    Wrap a Move entry
    MoveEntry moveEntry = new MoveEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            userName);
    System.out.println(serializer.serialize(moveEntry));
    return getActionAjaxResBodyResponseEntity(moveEntry);
  }

  @PostMapping(value = "/commit")
  public ResponseEntity<?> commit() throws IOException {
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
    ActionAjaxResBody resBody = new ActionAjaxResBody();
//    for local test
//    resBody.setValRes("test resolve combat result");
//    resBody.setGraphData(util.mockObjectNodes());
//    return ResponseEntity.status(HttpStatus.ACCEPTED).body(resBody);
    ClientSocket cs = playerMapping.getSocket(userName);
    if (cs.hasNewMsg()) {
//  1. recv combat result
      String combatRes = cs.recvMessage();
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
//          todo: redirect to winner's page
          resBody.setGraphData(null);
          resBody.setWin(true);
        }
      } else {
//        2.2 LOSE
//        todo: redirect to loser's page
        resBody.setGraphData(null);
        resBody.setWin(false);
      }
      resBody.setValRes(combatRes);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(resBody);
    }
//    Continue to wait for combating result
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }


  /**
   * Helper function for validating action with socket server
   * @param ae is the action entry
   * @return a response entity whose body is ActionAjaxResBody
   * @throws IOException
   */
  private ResponseEntity<ActionAjaxResBody> getActionAjaxResBodyResponseEntity(ActionEntry ae) throws IOException {
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
}
