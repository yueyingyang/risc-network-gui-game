package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

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
  protected final ActionParser parser;
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
    parser = new ActionParser();
    setupActionCreationFns();
    serializer = new JSONSerializer();
  }

  private void setupActionCreationFns() {
    actionCreationFns.put("a", parser::makeAttackEntry);
    actionCreationFns.put("p", parser::makePlaceEntry);
    actionCreationFns.put("m", parser::makeMoveEntry);
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
  }

  /**
   * Prompt to user to input action entry
   *
   * @param prompt is the prompt to instruct
   * @return is a ActionEntry object
   * @throws IOException if R/W exception
   */
  public ActionEntry readOneActionEntry(String prompt) throws IOException {
    userOut.println(prompt);
    ActionEntry ae = null;
    String line = userIn.readLine();
    if (line.contains("commit")) return null;
    try {
      ae = parseActionEntry(line);
    } catch (IllegalArgumentException e) {
      userOut.println(e.getMessage());
      line = userIn.readLine();
      parseActionEntry(line);
    }
    return ae;
  }

  /**
   * Call parser to parse a string into Action entry object
   *
   * @param line is the string to be parse
   * @return ActionEntry
   * @throws IOException if io exception
   */
  private ActionEntry parseActionEntry(String line) throws IOException {
    /*
      format: Type from To Unit
     */

    // split by space
    String type = line.split("\\s+")[0].toLowerCase(Locale.ROOT);
    if (!actionCreationFns.containsKey(type)) {
      throw new IllegalArgumentException("Type letter need to be (a)ttack, (m)ove, but now is " + type);
    }
    String actionEntryPart = line.split("\\s", 2)[1];
    return actionCreationFns.get(type).apply(actionEntryPart);
  }

  /**
   * Recv map and total units, instruct user to input placement
   *
   * @throws IOException if IO exception
   */
  public void placementPhase() throws IOException {
    List<ActionEntry> placementList = new ArrayList<>();
    GameMap m = this.recvMap();
    display(new MapView(m).displayMapShape());
    display("Here is the game map, and you have total units of " + this.recvMessage());
    Iterable<Territory> ts = m.getPlayerTerritories(name);
    for (Territory t : ts) {
      display("How many units you want to place on territory " + t.getName() + "?");
      placementList.add(parseActionEntry("p " + t.getName() + " " + readFromUser()));
    }
    sendMessage(serializer.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(placementList));
  }

  /**
   * Receive a string and parse into gamemap
   *
   * @return a GameMap
   * @throws IOException if IO exception
   */
  private GameMap recvMap() throws IOException {
    String mapJSON = recvMessage();
    return (GameMap) serializer.deserialize(mapJSON, GameMap.class);
  }

  /**
   * Recv map and read action entry, validate by server, update local map each time, then commit.
   *
   * @throws IOException if IO exception
   */
  public void playOneTurn() throws IOException {
    GameMap m = this.recvMap();
    display("CURRENT GAME MAP");
    display(new MapView(m).display());
    display("Hi here is your turn to place orders:\n" +
            "You can either move, attack, or commit for ending this order turn.\n" +
            "Here is the format example:\n" +
            "m a b 1 (means  (m)ove 1 unit from territory a to territory b)\n" +
            "a a b 1 (means  (a)ttack territory b using 1 unit from territory a)\n" +
            "commit  (means  you finish placing orders in this turn)\n");
    while (true) {
      ActionEntry a = readOneActionEntry("Please enter order(1 line at a time):");
      if (a == null) break;
      // server-side check and update
      sendMessage(serializer.serialize(a));
      String serverValidationResult = recvMessage();
      if (!serverValidationResult.equals(Constant.VALID_ACTION)) {
        display(serverValidationResult);
        continue;
      }
      try {
        a.apply(m, null);
        display(Constant.VALID_ACTION);
      } catch (IllegalArgumentException e) {
        display(e.getMessage());
      }
    }
    sendMessage(Constant.ORDER_COMMIT);
  }
}
