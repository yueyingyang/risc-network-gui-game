package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.PlayerInfo;
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
import java.util.Map;

/**
 * Controller to handle 4 types of actions + commit
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AjaxActionController {

  private final PlayerSocketMap playerMapping;
  private final UtilService util;
  private final JSONSerializer serializer;

  public AjaxActionController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.playerMapping = playerMapping;
    this.serializer = new JSONSerializer();
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
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(Constant.ORDER_COMMIT);
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
//    1. With server: send * 1 (action entry) + recv * 2 (validation result + mapView)
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(serializer.serialize(ae));
    String validRes = cs.recvMessage();
    String mapViewString = cs.recvMessage();
//    2. Wrap response
    Map<String, List<ObjectNode>> graphData = util.deNodeList(mapViewString);
    ActionAjaxResBody resBody = new ActionAjaxResBody();
    resBody.setGraphData(graphData);
    resBody.setValRes(validRes);
    return ResponseEntity.ok(resBody);
  }
}
