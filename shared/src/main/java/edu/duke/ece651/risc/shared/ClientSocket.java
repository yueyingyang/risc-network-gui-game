package edu.duke.ece651.risc.shared;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSocket {
  protected BufferedReader in;
  protected PrintWriter out;
  protected Socket s;
  private static final Logger LOGGER = Logger.getLogger(ClientSocket.class.getName());

  public ClientSocket(Socket s) throws IOException {
    this.s = s;
    this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    this.out = new PrintWriter(s.getOutputStream(), true);
  }

  /**
   * Write to output stream
   *
   * @param msg is the msg to write
   */
  public void sendMessage(String msg) {
    LOGGER.info(msg);
    out.println(msg);
  }

  /**
   * Read from input stream
   *
   * @return msg received
   * @throws IOException if readline throws exception
   */
  public String recvMessage() throws IOException {
    String rev = in.readLine();
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
    return in.ready();
  }

  /**
   * Close the socket
   *
   * @throws IOException
   */
  public void close() throws IOException {
    s.close();
  }

}
