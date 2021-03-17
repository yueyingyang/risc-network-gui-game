package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;

/**
 * ClientPlayer: Used on the client-side game
 */
public class ClientPlayer extends Player {
  /**
   * User's input and output stream
   */
  protected final BufferedReader userIn;
  protected final PrintStream userOut;
  protected final Map<String, Function<String, ActionEntry>> actionCreationFns;
  protected final JSONSerializer serializer;

  /**
   * @param in      is the reader to game server.
   * @param out     is the writer to game server. PrintWriter is good for
   *                transmitting STRING.
   * @param userIn  is the user input stream.
   * @param userOut is the user output stream.
   */
  public ClientPlayer(BufferedReader in, PrintWriter out, BufferedReader userIn, PrintStream userOut) {
    super(in, out);
    this.userIn = userIn;
    this.userOut = userOut;
    actionCreationFns = new HashMap<>();
    serializer = new JSONSerializer();
  }

  /**
   * @param s the string for the client to display
   */
  public void display(String s) {
    this.userOut.println(s);
  }

  /**
   * @return string read from stdin
   * @throws IOException if read/write exception
   */
  public String readFromUser() throws IOException {
    return this.userIn.readLine();
  }

  /**
   * Error handling for user's input:
   * continue to readinput until receive correctMsg
   * display correctMsg and go to next step after receive expected correctMsg
   *
   * @param correctMsg is the correct signal
   * @throws IOException if read/write exception
   */
  public void typeUntilCorrect(String correctMsg) throws IOException {
    sendMessage(readFromUser());
    String msg = recvMessage();
    while (!msg.equals(correctMsg)) {
      display(msg);
      sendMessage(readFromUser());
      msg = recvMessage();
    }
    display(msg);
  }

  /**
   * Player join the game: select a existed game to join or start a new game.
   *
   * @throws IOException if R/W exception
   */
  public void loginGame() throws IOException {
    // ask the player whether she/he wants to start a name game or join a game
    // different prompt when there's no available games to join
    String str = this.recvMessage();
    this.display(str);
    if (!str.equals(Constant.NO_GAME_AVAILABLE_INFO)) {
      // the user send s or j from stdin
      this.typeUntilCorrect(Constant.SUCCESS_ACTION_CHOOSED);
      // user type in how many player do you want/the available games list
    }
    this.display(this.recvMessage());
    this.typeUntilCorrect(Constant.SUCCESS_NUMBER_CHOOSED);
    String name = this.recvMessage();
    this.setName(name);
    display("You login as " + name + " successfully.");
  }

  /**
   * Recv map and total units, instruct user to input placement
   *
   * @throws IOException if IO exception
   */
  public void placementPhase() throws IOException {
    List<ActionEntry> placementList = new ArrayList<>();
    GameMap m = this.parseMap(recvMessage());
    display(new MapView(m).displayMapShape());
    int remainingUnit = Integer.parseInt(this.recvMessage());
    display("Here is the game map, and you have total units of " + remainingUnit);
    Iterable<Territory> ts = m.getPlayerTerritories(name);
    for (Territory t : ts) {
      int num = readUnitNumber("How many units(remaining: " + remainingUnit + ") you want to place on territory " + t.getName() + "?");
      while (num > remainingUnit) {
        num = readUnitNumber(num + " exceeds your remaining limit(" + remainingUnit + ")!\n");
      }
      remainingUnit -= num;
      placementList.add(new PlaceEntry(t.getName(), num));
    }
    sendMessage(serializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(placementList));
  }

  /**
   * Recv map and read action entry, validate by server, update local map each time, then commit.
   *
   * @throws IOException if IO exception
   */
  public void playOneTurn(String map) throws IOException {
    GameMap m = this.parseMap(map);
    display("Hi, " + this.getName() + "!\n" +
            "Here is current game map:");
    display(new MapView(m).display());
    while (true) {
      display("Possible actions options you can choose:\n" +
              "M Move units between your territories\n" +
              "A Attack other player territories \n" +
              "C Commit for ending this turn\n");
      String actionType = readActionType("Please enter the action type(M, A or C):");
      if (actionType.equals("c")) break;
      ActionEntry ae = readActionInformation(actionType);
      // parse action entry fail
      if (ae == null) continue;
      // server-side check and update
      sendObject(ae);
      String serverValidRes = recvMessage();
      display(serverValidRes);
      if (!serverValidRes.equals(Constant.VALID_ACTION)) {
        continue;
      }
      try {
        ae.apply(m, null);
        display("Updated game map:\n" + new MapView(m).display());
      } catch (IllegalArgumentException e) {
        // actually should not happen
        display("Failed to apply on current game map:" + e.getMessage());
      }
    }
    sendMessage(Constant.ORDER_COMMIT);
  }

  /**
   * User can watch the game after he lost and chose to watch
   *
   * @throws IOException if IOE
   */
  public void watchGame(String prompt) throws IOException {
    display(prompt);
    String selection = readFromUser().toUpperCase(Locale.ROOT);
    if (!selection.equals("E") && !selection.equals("C")) {
      watchGame("Please enter (E) or (C), but is " + selection);
      return;
    }
    if (selection.equals("E")) {
      display("Thanks for joining the game today, goodbye!");
      sendMessage(Constant.DISCONNECT_GAME);
      return;
    }
    // chose "C"
    sendMessage(Constant.WATCH_GAME);
    display("You start to watch the game:");
    while (true) {
      String recv = this.recvMessage();
      if (recv.equals(Constant.GAME_OVER)) {
        display(Constant.GAME_OVER);
        display(recvMessage());
        break;
      }
      display("Current game status:");
      GameMap m = this.parseMap(recv);
      display(new MapView(m).display());
      displayCombatRes();
    }
  }

  /*
    ======== Blow is some util functions read, send and parse ========
   */

  /**
   * Read action type from user input
   *
   * @param prompt to let user input
   * @return the action type string: currently should only be m, c, a.
   * @throws IOException if IO exception
   */
  private String readActionType(String prompt) throws IOException {
    display(prompt);
    String actionType = userIn.readLine();
    String type = actionType.toLowerCase(Locale.ROOT);
    if (!type.equals("m") && !type.equals("c") && !type.equals("a")) {
      return readActionType("Type letter need to be (a)ttack, (m)ove, (c)ommit but now is " + type + ". Please enter again:");
    }
    return type;
  }

  /**
   * Read territory name
   *
   * @param prompt to let user input
   * @return the name string
   * @throws IOException if IOE
   */
  private String readTerritoryName(String prompt) throws IOException {
    display(prompt);
    return userIn.readLine();
  }

  /**
   * Read unit number
   *
   * @param prompt for user to type / re-type
   * @return an int
   * @throws IOException if IOE
   */
  private Integer readUnitNumber(String prompt) throws IOException {
    display(prompt);
    int unit;
    String s = readFromUser();
    try {
      unit = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return readUnitNumber("The soldier number should be an integer:" + e.getMessage());
    }
    return unit;
  }

  /**
   * Not elegant way to read and parse an action
   *
   * @param actionType is the action type to decide the prompt and ActionEntry subtype
   * @return an ActionEntry instance
   * @throws IOException if IOE
   */
  private ActionEntry readActionInformation(String actionType) throws IOException {
    // sorry for this RY, I just cannot figure out an easy but elegant way to do this
    if (actionType.equals("m")) {
      String fromTerritory = readTerritoryName("[Move] Which territory want to move from?");
      String toTerritory = readTerritoryName("[Move] Which territory want to move to?");
      Integer unitNum = readUnitNumber("[Move] How many soldiers you want move from " + fromTerritory + "to " + toTerritory + "?");
      return new MoveEntry(fromTerritory, toTerritory, unitNum);
    } else if (actionType.equals("a")) {
      String fromTerritory = readTerritoryName("[Attack] From which territory you want to send soldiers out?");
      String toTerritory = readTerritoryName("[Attack] Which territory you want to attack?");
      Integer unitNum = readUnitNumber("[Attack] How many soldiers you want to use from " + toTerritory + "?");
      return new AttackEntry(fromTerritory, toTerritory, unitNum);
    } else {
      return null;
    }
  }

  /**
   * Send the serialized object, currently is sending out the JSON string
   *
   * @param o is the object to be serialize and send
   */
  private void sendObject(Object o) {
    sendMessage(serializer.serialize(o));
  }


  /**
   * Receive a string and parse into game map
   *
   * @return a GameMap
   */
  private GameMap parseMap(String mapJSON) {
    return (GameMap) serializer.deserialize(mapJSON, GameMap.class);
  }

  public void displayCombatRes() throws IOException {
    display((String) serializer.deserialize(recvMessage(), String.class));
  }
}
