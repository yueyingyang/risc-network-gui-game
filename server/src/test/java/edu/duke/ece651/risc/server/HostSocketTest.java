package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HostSocketTest {
  @Test
  public void test_closeSocket() {
    HostSocket hs = new HostSocket(4444);
    assertDoesNotThrow(hs::closeSocket);
  }

  @Test
  public void test_getSocket() {
    HostSocket hs = new HostSocket(4444);
    assertDoesNotThrow(() -> hs.getSocket().close());
  }

  @Test
  public void test_constructorException() {
    PrintStream standardOut = System.out;
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));
    HostSocket hs = new HostSocket(6666);
    assertDoesNotThrow(() -> new HostSocket(6666));
    System.setOut(standardOut);
  }

}



