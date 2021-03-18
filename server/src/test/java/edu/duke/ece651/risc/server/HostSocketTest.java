package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class HostSocketTest {
  @Test
  public void test_closeSocket() {
    HostSocket hs = new HostSocket(4444);
    assertDoesNotThrow(()->{hs.closeSocket();});
  }

  @Test
  public void test_getSocket(){
    HostSocket hs = new HostSocket(4444);
    assertDoesNotThrow(()->{hs.getSocket().close();});
  }

  @Test
  public void test_constructorException() {
    PrintStream standardOut = System.out;
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));
    HostSocket hs = new HostSocket(80);
    assertEquals("Exception caught when listening on port80\nPermission denied\n",outputStreamCaptor.toString());
    System.setOut(standardOut);
  }

}
