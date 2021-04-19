package edu.duke.ece651.risc.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece651.risc.shared.ClientSocket;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.*;
import edu.duke.ece651.risc.web.model.ActionAjaxResBody;
import edu.duke.ece651.risc.web.model.ToolCreation;
import edu.duke.ece651.risc.web.model.UserActionInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
  private final Map<String, ToolCreation> toolEntryMapping;

  public AjaxActionController(PlayerSocketMap playerMapping, UtilService util) {
    this.util = util;
    this.playerMapping = playerMapping;
    this.serializer = new JSONSerializer();
    this.toolEntryMapping = initEntryMapping();
  }

  private Map<String, ToolCreation> initEntryMapping() {
    Map<String, ToolCreation> m = new HashMap<>();
    m.put("cloaking", CloakEntry::new);
    return m;
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
    // Wrap a Attack entry
    FancyAttackEntry attackEntry = new FancyAttackEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            input.getFromType(),
            userName);
    return getActionRes(attackEntry);
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
    // Wrap a Move entry
    FancyMoveEntry moveEntry = new FancyMoveEntry(input.getFromName(),
            input.getToName(),
            input.getSoldierNum(),
            input.getFromType(),
            userName);
    return getActionRes(moveEntry);
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
    // Wrap a Soldier entry
    SoldierEntry soldierEntry = new SoldierEntry(
            input.getToName(),
            input.getFromType(),
            input.getToType(),
            input.getSoldierNum(),
            userName);
    return getActionRes(soldierEntry);
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
    // Wrap a Tech entry
    TechEntry techEntry = new TechEntry(
            userName);
    return getActionRes(techEntry);
  }

  /**
   * Buy action
   *
   * @param input
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/buy", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> buy(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    // todo: Wrap a Product Entry
    // TechEntry techEntry = new TechEntry(userName);
    System.out.println("type: " + input.getFromType().toLowerCase(Locale.ROOT) + " soldierNum: " + input.getSoldierNum());
    return getActionRes(null);
  }

  /**
   * Move spy action
   *
   * @param input
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/move_spy", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> moveSpy(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    SpyMoveEntry spyMove = new SpyMoveEntry(input.getFromName(), input.getToName(), input.getSoldierNum(), userName);
    return getActionRes(spyMove);
  }

  /**
   * Research cloaking action
   *
   * @param input
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/res_cloaking", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> resCloaking(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    return getActionRes(new CloakingTechEntry(userName));
  }

  /**
   * Upgrade to spy action
   *
   * @param input
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/spy", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> spy(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    SpyEntry spy = new SpyEntry(input.getToName(), input.getSoldierNum(), userName);
    return getActionRes(spy);
  }

  /**
   * Apply tools action
   *
   * @param input
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/tools", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActionAjaxResBody> tool(@RequestBody UserActionInput input) throws IOException {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    return getActionRes(toolEntryMapping.get(input.getFromType()).apply(input.getToName(), userName));
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
  private ResponseEntity<ActionAjaxResBody> getActionRes(ActionEntry ae) throws IOException {
    // 1. With server: send * 1 (action entry) + recv * 2 (validation result + mapView)
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    ClientSocket cs = playerMapping.getSocket(userName);
    cs.sendMessage(serializer.serialize(ae));
    String validRes = cs.recvMessage();
    String mapViewString = cs.recvMessage();
    // 2. Wrap response
    Map<String, List<ObjectNode>> graphData = util.deNodeList(mapViewString);
    ActionAjaxResBody resBody = new ActionAjaxResBody();
    resBody.setGraphData(graphData);
    resBody.setValRes(validRes);
    return ResponseEntity.ok(resBody);
  }
}
