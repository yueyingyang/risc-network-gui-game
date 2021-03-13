/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.ServerPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;

class AppTest {
  App app;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    app = new App(hs, out);
  }

  @Mock
  private PrintStream out;

  @Mock
  private ServerSocket ss;

  @Mock
  private HostSocket hs;

  @Mock
  private ServerPlayer player;

  @Test
  void test_start_new_game() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer sp = new ServerPlayer(new BufferedReader(new StringReader("2\n")),
            new PrintWriter(bytes, true));
    app.startNewGame(sp);
    assertEquals("Hi ^_^, We will create a new game for you. How many players do you want to have in your Game?\n" +
            "You are in a Game now!\n" +
            "Red\n", bytes.toString());
  }

  @Test
  void test_get_available_game() throws IOException {
    Mockito.when(player.readGameSize()).thenReturn(3);
    app.startNewGame(player);
    assertEquals(1, app.getAvailableGames().size());
  }

  @Test
  void test_print_available_gameList() throws IOException {
    Mockito.when(player.readGameSize()).thenReturn(3);
    app.startNewGame(player);
    assertEquals("The available games here are: 0 ", app.printAvailableGameList());
  }

  @Test
  void test_join_exist_game() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer sp = new ServerPlayer(new BufferedReader(new StringReader("a\n1\n0\n")),
            new PrintWriter(bytes, true));
    Mockito.when(player.readGameSize()).thenReturn(2);
    app.startNewGame(player);
    app.joinExistingGame(sp);
    assertEquals(0, app.getAvailableGames().size());
    String expected = "The available games here are: 0 \n" +
            "You should type a valid number!\n" +
            "You should only chose from the available list!\n" +
            "You are in a Game now!\n" +
            "Blue\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_handle_incoming_request_j() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer sp = new ServerPlayer(new BufferedReader(new StringReader("j\na\n1\n0\n")),
            new PrintWriter(bytes, true));
    Mockito.when(player.readGameSize()).thenReturn(2);
    app.startNewGame(player);
    app.handleIncomeRequest(sp);
    assertEquals(0, app.getAvailableGames().size());
    String expected = "Hi, Do you want to start a new game(type s) or join an existing game(type j)?\n" +
            "Successfully choose an action!\n" +
            "The available games here are: 0 \n" +
            "You should type a valid number!\n" +
            "You should only chose from the available list!\n" +
            "You are in a Game now!\n" +
            "Blue\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_handle_incoming_request_no_available() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer sp = new ServerPlayer(new BufferedReader(new StringReader("j\na\n1\n3\n")),
            new PrintWriter(bytes, true));
    assertEquals(0, app.getAvailableGames().size());
    app.handleIncomeRequest(sp);
    String expected = "Hi, there's no available game in the system, so we will start a game for you.\n" +
            "Hi ^_^, We will create a new game for you. How many players do you want to have in your Game?\n" +
            "You should type a valid number!\n" +
            "You should type a valid number!\n" +
            "You should type a valid number!\n" +
            "You are in a Game now!\n" +
            "Red\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_handle_incoming_request_s() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer sp = new ServerPlayer(new BufferedReader(new StringReader("s\n2\n")),
            new PrintWriter(bytes, true));
    Mockito.when(player.readGameSize()).thenReturn(2);
    app.startNewGame(player);
    assertEquals(1, app.getAvailableGames().size());
    app.handleIncomeRequest(sp);

    String expected = "Hi, Do you want to start a new game(type s) or join an existing game(type j)?\n" +
            "Successfully choose an action!\n" +
            "Hi ^_^, We will create a new game for you. How many players do you want to have in your Game?\n" +
            "You are in a Game now!\n" +
            "Red\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_accept_player() throws IOException, InterruptedException {
    Socket cs = Mockito.mock(Socket.class);
    String clientIn = "j\na\n1\n3\n";
    InputStream in = new ByteArrayInputStream(clientIn.getBytes());
    OutputStream out = new ByteArrayOutputStream();
    Mockito.when(cs.getInputStream()).thenReturn(in);
    Mockito.when(cs.getOutputStream()).thenReturn(out);
    Mockito.when(ss.accept()).thenReturn(cs);
    Thread t = new Thread(() -> {
      app.acceptPlayers(ss);
    });
    // wait for "acceptPlayers" finishing
    Thread.sleep(2000);
    // check the player's out - it should have sth???
    assertEquals("", out.toString());
    // end the acceptPlayers
    t.interrupt();
    t.join();
  }
}
