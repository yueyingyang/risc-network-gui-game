package edu.duke.ece651.risc.shared;

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
            Constant.SUCCESS_NUMBER_CHOOSED;
    String userIn = "3\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(serverIn + "\n", userOut.toString());
  }

  @Test
  void test_login_game_join_existed_game() throws IOException {
    String serverIn = Constant.ASK_START_NEW_OR_JOIN + "\n" + Constant.SUCCESS_ACTION_CHOOSED + "\n" + "fake list of games" + "\n" + Constant.SUCCESS_NUMBER_CHOOSED;
    String userIn = "j\n1\n";
    ClientPlayer p = createClientPlayer(serverIn,
            serverOut,
            userIn,
            userOut);
    p.loginGame();
    assertEquals(userIn, serverOut.toString());
    assertEquals(serverIn + "\n", userOut.toString());
  }

  public ClientPlayer createClientPlayer(String serverIn, ByteArrayOutputStream serverOut, String userIn, ByteArrayOutputStream userOut) {
    return new ClientPlayer(new BufferedReader(new StringReader(serverIn)),
            new PrintWriter(serverOut, true),
            new BufferedReader(new StringReader(userIn)),
            new PrintStream(userOut));
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
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    Serializer s = new JSONSerializer();
    String result = s.serialize(map);
    ClientPlayer p = createClientPlayer(result + "\n" + 4,
            serverOut,
            "1\n5\n3\n",
            userOut);
    p.setName("player1");
    p.placementPhase();
    assertEquals("[{\"type\":\"place\",\"terrName\":\"0\",\"numSoldiers\":1},{\"type\":\"place\",\"terrName\":\"1\",\"numSoldiers\":3}]\n", serverOut.toString());
  }

  @Test
  void test_play_one_turn() throws IOException {
    GameMap map = createMap();
    Serializer s = new JSONSerializer();
    String mapjson = s.serialize(map);
    ClientPlayer p = createClientPlayer(Constant.VALID_ACTION + "\n" + "Action is invalid\n" + Constant.VALID_ACTION + "\n",
            serverOut,
            "b\nm\n0\n1\n1\nm\n0\n1\n1\na\n1\n2\na\n1\nc\n",
            userOut);
    p.setName("player1");
    assertDoesNotThrow(() -> p.playOneTurn(mapjson + "\n"));
  }

  private GameMap createMap() {
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 2),
            new PlaceEntry("1", 2),
            new PlaceEntry("2", 2),
            new PlaceEntry("3", 2));
    for (ActionEntry ae : pl) {
      ae.apply(map, null);
    }
    return map;
  }

  @Test
  void test_watch_game() throws IOException {
    GameMap map = createMap();
    ClientPlayer p = createClientPlayer(Constant.GAME_OVER + "\nwinner is\n" + new JSONSerializer().serialize(map) + "\n" + Constant.GAME_OVER + "\nwinner is\n",
            serverOut,
            "a\ne\nc\n",
            userOut);
    p.watchGame("Please choose");
    assertEquals("Please choose\n" +
            "Please enter (E) or (C), but is A\n" +
            "You start to watch the game:\n" +
            "Game over ~\n" +
            "winner is\n", userOut.toString());
    userOut.reset();
    p.watchGame("Please choose");
    assertEquals("Please choose\n" +
            "You start to watch the game:\n" +
            "Current game status:\n" +
            "player1 player:\n" +
            "-------------\n" +
            "2 units in 0 (next to: 1, 2, 3)\n" +
            "2 units in 1 (next to: 0, 2, 3)\n" +
            "\n" +
            "player2 player:\n" +
            "-------------\n" +
            "2 units in 2 (next to: 0, 1, 3)\n" +
            "2 units in 3 (next to: 0, 1, 2)\n" +
            "\n" +
            "\n" +
            "Game over ~\n" +
            "winner is\n", userOut.toString());
  }

}