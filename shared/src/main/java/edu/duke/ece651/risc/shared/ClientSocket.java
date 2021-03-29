package edu.duke.ece651.risc.shared;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSocket {
  protected InputStream in;
  protected OutputStream out;
  private static final Logger LOGGER = Logger.getLogger(ClientSocket.class.getName());

  public ClientSocket(Socket s) throws IOException {
    this.in = s.getInputStream();
    this.out = s.getOutputStream();
  }

  /**
   * Write to output stream
   *
   * @param msg is the msg to write
   */
  public void sendMessage(String msg) {
    LOGGER.info(msg);
    new PrintWriter(out, true).println(msg);
  }

  /**
   * Read from input stream
   *
   * @return msg received
   * @throws IOException if readline throws exception
   */
  public String recvMessage() throws IOException {
    String rev = new BufferedReader(new InputStreamReader(in)).readLine();
    LOGGER.info(rev);
    return rev;
  }

  /**
   * Check if socket input stream has new msg
   *
   * @return if socket input stream has new msg
   * @throws IOException if IO
   */
  public boolean hasNewMsg() throws IOException {
    return in.available() > 0;
  }
}
