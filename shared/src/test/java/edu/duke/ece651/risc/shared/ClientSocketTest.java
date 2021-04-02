package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientSocketTest {
  @Test
  void test_send() throws IOException {
    String clientIn = "1\n2\n";
    Socket s = Mockito.mock(Socket.class);
    OutputStream out = getMockClientOutput(s, clientIn);
    ClientSocket cs = new ClientSocket(s);
    String expectedOut = "I'm okay, I'm happy coding";
    cs.sendMessage(expectedOut);
    assertEquals(expectedOut + "\n", out.toString());
  }

  @Test
  void test_recv() throws IOException {
    String clientIn = "1\n2\n";
    Socket s = Mockito.mock(Socket.class);
    OutputStream out = getMockClientOutput(s, clientIn);
    ClientSocket cs = new ClientSocket(s);
    assertEquals("1", cs.recvMessage());
  }

  @Test
  void test_available() throws IOException {
    String clientIn = "1\n2\n";
    Socket s = Mockito.mock(Socket.class);
    OutputStream out = getMockClientOutput(s, clientIn);
    ClientSocket cs = new ClientSocket(s);
    assertEquals("1", cs.recvMessage());
    assertTrue(cs.hasNewMsg());
  }

  private OutputStream getMockClientOutput(Socket cs, String clientIn) throws IOException {
    InputStream in = new ByteArrayInputStream(clientIn.getBytes());
    OutputStream out = new ByteArrayOutputStream();
    Mockito.when(cs.getInputStream()).thenReturn(in);
    Mockito.when(cs.getOutputStream()).thenReturn(out);
    return out;
  }
}