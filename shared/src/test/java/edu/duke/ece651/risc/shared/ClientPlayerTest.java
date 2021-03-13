package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientPlayerTest {
  ByteArrayOutputStream serverOut;
  ByteArrayOutputStream userOut;

  // run before each test to initialize outputstream
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
    ByteArrayOutputStream serverOut = new ByteArrayOutputStream();
    ByteArrayOutputStream userOut = new ByteArrayOutputStream();
    ClientPlayer p = createClientPlayer("read from server\nIt's correct", serverOut, "read from user\n ohh\n", userOut);
    p.typeUntilCorrect("It's correct");
    assertEquals("read from server\n" +
            "It's correct\n", userOut.toString());
  }

  @Test
  void test_login_game_no_available_game() throws IOException {
    ByteArrayOutputStream serverOut = new ByteArrayOutputStream();
    ByteArrayOutputStream userOut = new ByteArrayOutputStream();
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
    ByteArrayOutputStream serverOut = new ByteArrayOutputStream();
    ByteArrayOutputStream userOut = new ByteArrayOutputStream();
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

  private ClientPlayer createClientPlayer(String serverIn, ByteArrayOutputStream serverOut, String userIn, ByteArrayOutputStream userOut) {
    return new ClientPlayer(new BufferedReader(new StringReader(serverIn)),
            new PrintWriter(serverOut, true),
            new BufferedReader(new StringReader(userIn)),
            new PrintStream(userOut));
  }

  @Test
  void test_get_name() {
    ByteArrayOutputStream serverOut = new ByteArrayOutputStream();
    ByteArrayOutputStream userOut = new ByteArrayOutputStream();
    ClientPlayer p = createClientPlayer("",
            serverOut,
            "",
            userOut);
    p.setName("test_player");
    assertEquals("test_player", p.getName());
  }
}