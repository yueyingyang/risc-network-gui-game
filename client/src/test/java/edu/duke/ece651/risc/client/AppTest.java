
package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AppTest {
  String loginServerIn;
  String loginUserIn;
  String placementServerIn;
  String placementUserIn;
  String attackServerIn;
  String attackUserOut;
  String easyMapJSON;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    GameMap map = createMap();
    Serializer serializer = new JSONSerializer();
    String result = serializer.serialize(map);
    GameMap easyMap = createEasyMap();
    easyMapJSON = serializer.serialize(easyMap);

    loginServerIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED + "\n" + "name\n";
    loginUserIn = "3\n";
    placementServerIn = result + "\n" + 4 + "\n";
    placementUserIn = "1\n2\n";
    attackServerIn = easyMapJSON + "\n" + Constant.VALID_ACTION + "\n" +
            "\"combat result\"\n" + Constant.LOSE_GAME + "\n" + Constant.GAME_OVER;
    attackUserOut = "a\n0\n1\n1\nc\ne\n";
  }

  @Mock
  private PrintStream userOut;

  @Spy
  private OutputStream mockOutputStream;

  @Test
  void test_login() throws IOException {
    // mock preparation
    Socket s = Mockito.mock(Socket.class);
    App app = getApp(s, loginServerIn, loginUserIn);
    app.loginGame();
  }

  private App getApp(Socket s, String serverIn, String userInString) throws IOException {
    InputStream mockInputStream = new ByteArrayInputStream(serverIn.getBytes());
    Mockito.when(s.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(s.getOutputStream()).thenReturn(mockOutputStream);
    BufferedReader userInputStream = new BufferedReader(new StringReader(userInString));
    return new App(s, userInputStream, userOut);
  }

  @Test
  void test_end_game() throws IOException {
    // mock preparation
    Socket s = Mockito.mock(Socket.class);
    App app = getApp(s, "", "");
    app.endGame();
    Mockito.verify(s).close();
  }

  @Test
  void test_placement() throws IOException {
    Socket s = Mockito.mock(Socket.class);
    App app = getApp(s, placementServerIn, placementUserIn);
    app.placementPhase();
    Mockito.verify(mockOutputStream).flush();
  }

  @Test
  void test_attack() throws IOException {
    Socket s = Mockito.mock(Socket.class);
    // play 2 turns and win
    App app = getApp(s,
            easyMapJSON + "\n" + Constant.VALID_ACTION + "\n" +
                    "\"combat result\"\n" + Constant.CONTINUE_PLAYING + "\n" +
                    easyMapJSON + "\n" + Constant.VALID_ACTION + "\n\"combat result\"\n" + Constant.CONTINUE_PLAYING + "\n" +
                    Constant.GAME_OVER + "\n winner\n",
            "a\n0\n1\n1\nc\na\n0\n1\n1\nc\n");
    assertDoesNotThrow(app::attackPhase);
    // lost and exit
    App app2 = getApp(s, attackServerIn, attackUserOut);
    assertDoesNotThrow(app2::attackPhase);
    // lost but continue to want
    App app3 = getApp(s, easyMapJSON + "\n" + Constant.VALID_ACTION + "\n" +
                    "\"combat result\"\n" + Constant.LOSE_GAME + "\n" + Constant.CONTINUE_PLAYING + "\n" + easyMapJSON + "\n" + "\"resolve combat\"\n" + Constant.GAME_OVER,
            "a\n0\n1\n1\nc\nc\n");
    assertDoesNotThrow(app3::attackPhase);
  }

  @Test
  void test_run() throws IOException {
    GameMap map = createEasyMap();
    Serializer serializer = new JSONSerializer();
    String mapJSON = serializer.serialize(map);
    Socket s = Mockito.mock(Socket.class);
    // play 2 turns and win
    App app = getApp(s, loginServerIn + placementServerIn + attackServerIn,
            loginUserIn + placementUserIn + attackUserOut);
    assertDoesNotThrow(app::run);
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

  public static GameMap createEasyMap() {
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 1);
    List<ActionEntry> pl = Arrays.asList(new PlaceEntry("0", 1, "player1"),
            new PlaceEntry("1", 0, "player2"));
    for (ActionEntry ae : pl) {
      ae.apply(map, null);
    }
    return map;
  }
}
