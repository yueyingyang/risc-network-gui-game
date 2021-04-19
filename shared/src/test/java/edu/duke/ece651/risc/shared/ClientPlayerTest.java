package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientPlayerTest {
  ByteArrayOutputStream serverOut;
  ByteArrayOutputStream userOut;

  // run before each test to initialize output stream
  @BeforeEach
  private void initEach() {
    serverOut = new ByteArrayOutputStream();
    userOut = new ByteArrayOutputStream();
  }

  @Test
  void test_constructor() throws IOException {
    ClientPlayer p = createClientPlayer("Hello Server", serverOut, "UserInput", userOut);
    assertEquals("Hello Server", p.recvMessage());
    p.sendMessage("Hello Client 1");
    assertEquals("Hello Client 1\n", serverOut.toString());
    assertEquals("UserInput", p.readFromUser());
    p.display("Display it on the client side program");
    assertEquals("Display it on the client side program\n", userOut.toString());
  }

  @Test
  void test_type_until_correct() throws IOException {
    ClientPlayer p = createClientPlayer("read from server\nIt's correct", serverOut, "read from user\n ohh\n", userOut);
    p.typeUntilCorrect("It's correct");
    assertEquals("read from server\n" +
            "It's correct\n", userOut.toString());
  }

  @Test
  void test_login_game_no_available_game() throws IOException {
    String serverIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED + "\n" + "test player\n";
    String userPrint = "Hi, there's no available game in the system, so we will start a game for you.\n" +
            "Hi ^_^, We will create a new game for you. How many players do you want to have in your Game?\n" +
            "You are in a Game now!\n" +
            "You login as test player successfully.\n";
    String userIn = "3\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(userPrint, userOut.toString());
  }

  @Test
  void test_login_game_join_existed_game() throws IOException {
    String serverInstruction = Constant.ASK_START_NEW_OR_JOIN + "\n" + Constant.SUCCESS_ACTION_CHOOSED + "\n" + "fake list of games" + "\n" + Constant.SUCCESS_NUMBER_CHOOSED + "\n";
    String serverIn = serverInstruction + "fake player\n";
    String userOutput = serverInstruction + "You login as fake player successfully.\n";

    String userIn = "j\n1\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(userOutput, userOut.toString());
  }


  @Test
  void test_get_name() {
    ClientPlayer p = createClientPlayer("",
            serverOut,
            "",
            userOut);
    p.setName("test_player");
    assertEquals("test_player", p.getName());
  }

  @Test
  void test_placement() throws IOException {
    GameMap map = createMap();
    Serializer s = new JSONSerializer();
    String result = s.serialize(map);
    ClientPlayer p = createClientPlayer(result + "\n" + 4,
            serverOut,
            "1\n5\n1a\n-2\n2\n",
            userOut);
    p.setName("player1");
    p.placementPhase();
    String expect = "[{\"type\":\"place\",\"toName\":\"0\",\"numSoldiers\":1,\"playerName\":\"player1\",\"fromName\":null,\"fromType\":null,\"toType\":null,\"useShip\":false,\"numProds\":0,\"prodName\":null},{\"type\":\"place\",\"toName\":\"1\",\"numSoldiers\":2,\"playerName\":\"player1\",\"fromName\":null,\"fromType\":null,\"toType\":null,\"useShip\":false,\"numProds\":0,\"prodName\":null}]\n";
    assertEquals(expect, serverOut.toString());
  }

  @Test
  void test_play_one_turn() throws IOException {
    GameMap map = createMap();
    Serializer s = new JSONSerializer();
    String mapJSON = s.serialize(map);
    // corner case
    ClientPlayer p = createClientPlayer(Constant.VALID_ACTION + "\n" + "Action is invalid\n" + Constant.VALID_ACTION + "\n",
            serverOut,
            "b\nm\n0\n1\n1\nm\n0\n1\n1\na\n1\n2\na\n1\nc\n",
            userOut);
    p.setName("player1");
    assertDoesNotThrow(() -> p.playOneTurn(mapJSON + "\n"));
    // valid attack and move
    ClientPlayer p2 = createClientPlayer( Constant.VALID_ACTION + "\n" + Constant.VALID_ACTION + "\n",
            serverOut,
            "a\n1\n2\n1\nm\n0\n1\n1\nc\n",
            userOut);
    p2.setName("player1");
    assertDoesNotThrow(() -> p2.playOneTurn(mapJSON + "\n"));
    // exception wrong validation result
    ClientPlayer p3 = createClientPlayer( Constant.VALID_ACTION + "\n",
            serverOut,
            "a\n2\n3\n1\nc\n",
            userOut);
    p3.setName("player1");
    assertDoesNotThrow(() -> p3.playOneTurn(mapJSON + "\n"));
  }

  @Test
  void test_watch_game() throws IOException {
    GameMap map = createMap();
    String testExit = "";
    String testWatch = new JSONSerializer().serialize(map) + "\n" + "\"combat result\"\n" + Constant.GAME_OVER + "\nwinner is\n";
    ClientPlayer p = createClientPlayer(testExit + testWatch,
            serverOut,
            "a\ne\nc\n",
            userOut);
    p.watchGame("Please choose");
    assertEquals("Please choose\n" +
            "Please enter (E) or (C), but is A\n" +
            "Thanks for joining the game today, goodbye!\n", userOut.toString());
    userOut.reset();
    assertDoesNotThrow(() -> p.watchGame("Please choose"));
    assertEquals("Please choose\n" +
            "You start to watch the game:\n" +
            "Current game status:\n" +
            new MapView(map).display() +
            "\n" +
            "combat result\n" +
            "Game over ~\n" +
            "winner is\n", userOut.toString());
  }

  public static ClientPlayer createClientPlayer(String serverIn, ByteArrayOutputStream serverOut, String userIn, ByteArrayOutputStream userOut) {
    return new ClientPlayer(new BufferedReader(new StringReader(serverIn)),
            new PrintWriter(serverOut, true),
            new BufferedReader(new StringReader(userIn)),
            new PrintStream(userOut));
  }

  public static GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2, "player1"),
            new PlaceEntry("1", 2, "player1"),
            new PlaceEntry("2", 2, "player2"),
            new PlaceEntry("3", 2, "player2"));
    for (ActionEntry ae : pl) {
      ae.apply(map, null);
    }
    return map;
  }

}