package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class AjaxController {

  private final PlayerSocketMap playerMapping;
  private final UtilService util;

  // todo: change after user login
  private String currentUserName;


  public AjaxController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.currentUserName = "test";
    this.playerMapping = playerMapping;
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
    ClientSocket cs = playerMapping.getSocket(currentUserName);
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
    ClientSocket cs = playerMapping.getSocket(currentUserName);
    if (cs.hasNewMsg()) {
      String mapViewString = cs.recvMessage();
      List<ObjectNode> graphData = util.deNodeList(mapViewString);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(graphData);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
}
