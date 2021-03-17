
package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

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
    s.getInputStream();
    // mock prepration
    String serverIn = Constant.NO_GAME_AVAILABLE_INFO + "\n" +
            Constant.ASK_HOW_MANY_PLAYERS + "\n" +
            Constant.SUCCESS_NUMBER_CHOOSED + "\n" + "name";
    InputStream mockInputStream = new ByteArrayInputStream(serverIn.getBytes());
    Mockito.when(s.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(s.getOutputStream()).thenReturn(mockOutputStream);
    Mockito.when(userIn.readLine()).thenReturn("3");

    App app = new App(s, userIn, userOut);
    app.loginGame();
//    Mockito.verify(s).close();
  }

}
