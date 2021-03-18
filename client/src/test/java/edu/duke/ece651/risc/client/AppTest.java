
package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private Socket s;

  @Mock
  private BufferedReader userIn;

  @Mock
  private PrintStream userOut;

  @Mock
  private OutputStream mockOutputStream;

  @Test
  void test_app() throws IOException {
    // mock preparation
    String serverIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED + "\n" + "name";
    InputStream mockInputStream = new ByteArrayInputStream(serverIn.getBytes());
    Mockito.when(s.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(s.getOutputStream()).thenReturn(mockOutputStream);
    Mockito.when(userIn.readLine()).thenReturn("3");

    App app = new App(s, userIn, userOut);
    app.loginGame();
  }

  @Test
  void test_end_game() throws IOException {
    // mock preparation
    InputStream mockInputStream = new ByteArrayInputStream("".getBytes());
    Mockito.when(s.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(s.getOutputStream()).thenReturn(mockOutputStream);
    Mockito.when(userIn.readLine()).thenReturn("");

    App app = new App(s, userIn, userOut);
    app.endGame();
    Mockito.verify(s).close();
  }

  @Test
  void test_placement() {
//    GameMap map = createMap();
//    Serializer s = new JSONSerializer();
//    String result = s.serialize(map);
//    ClientPlayer p = createClientPlayer(result + "\n" + 4,
//            serverOut,
//            "1\n5\n3\n",
//            userOut);
//    p.setName("player1");
//    p.placementPhase();
//    String expect = "[{\"type\":\"place\",\"toName\":\"0\",\"numSoldiers\":1,\"fromName\":null}," +
//            "{\"type\":\"place\",\"toName\":\"1\",\"numSoldiers\":3,\"fromName\":null}]\n";
//    assertEquals(expect, serverOut.toString());
  }
}
