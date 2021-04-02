package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.AttackEntry;
import edu.duke.ece651.risc.shared.entry.MoveEntry;
import edu.duke.ece651.risc.web.model.ActionAjaxResBody;
import edu.duke.ece651.risc.web.model.MoveInput;
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
  private final JSONSerializer serialier;

  // todo: change after user login
  private String userName;


  public AjaxController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.userName = "test";
    this.playerMapping = playerMapping;
    this.serialier = new JSONSerializer();
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
   * todo: need to be moved to AjaxController after refactoring with V2MapView
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
  public ResponseEntity<ActionAjaxResBody> attack(@RequestBody MoveInput input) throws IOException {
//    Wrap a Attack entry
    AttackEntry attackEntry = new AttackEntry(input.getFromName(),
            input.getToName(),
            Integer.parseInt(input.getSoldierNum()),
            userName);
    System.out.println(serialier.serialize(attackEntry));
//    Send to server
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(serialier.serialize(attackEntry));
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
