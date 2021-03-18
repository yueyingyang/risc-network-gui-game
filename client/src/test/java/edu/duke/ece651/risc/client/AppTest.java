
package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.*;
import java.net.Socket;

import static edu.duke.ece651.risc.shared.TestHelper.createEasyMap;
import static edu.duke.ece651.risc.shared.TestHelper.createMap;

class AppTest {
  OutputStream testOutStream = new ByteArrayOutputStream();
  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private PrintStream userOut;

  @Spy
  private OutputStream mockOutputStream;

  @Test
  void test_app() throws IOException {
    // mock preparation
    String serverIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED + "\n" + "name";
    Socket s = Mockito.mock(Socket.class);
    App app = getApp(s, serverIn, "3");
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
    GameMap map = createMap();
    Serializer serializer = new JSONSerializer();
    String result = serializer.serialize(map);
    Socket s = Mockito.mock(Socket.class);
    App app = getApp(s, result + "\n" + 4, "1\n2\n");

    app.placementPhase();
    Mockito.verify(mockOutputStream).flush();
  }

  @Test
  void test_attack() throws IOException {
    GameMap map = createEasyMap();
    Serializer serializer = new JSONSerializer();
    String mapJSON = serializer.serialize(map);
    Socket s = Mockito.mock(Socket.class);
    // play 2 turn and win
    App app2 = getApp(s,
            mapJSON + "\n" + Constant.VALID_ACTION + "\n" +
                    "\"combat result\"\n" + Constant.CONTINUE_PLAYING + "\n" +
                    mapJSON + "\n" + Constant.VALID_ACTION + "\n\"combat result\"\n" +  Constant.CONTINUE_PLAYING + "\n" +
                    Constant.GAME_OVER + "\n winner\n",
            "a\n0\n1\n1\nc\na\n0\n1\n1\nc\n");
    app2.attackPhase();
  }
}
