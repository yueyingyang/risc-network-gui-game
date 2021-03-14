package edu.duke.ece651.risc.shared;

import java.io.*;

/**
 * This is the abstract player: support recv and send message
 */
public abstract class Player {
  // IO stream with game server
  protected BufferedReader in;
  protected PrintWriter out;
  protected String name;

  /**
   * Set name on players
   *
   * @param name is the name to be set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Constructor
   *
   * @param in  is inputstream the Game server and client.
   * @param out is outputstream the Game server and client.
   */
  public Player(BufferedReader in, PrintWriter out) {
    this.in = in;
    this.out = out;
  }

  public String getName() {
    return this.name;
  }

  /**
   * Write to output stream
   *
   * @param msg is the msg to write
   */
  public void sendMessage(String msg) {
    out.println(msg);
  }

  /**
   * Read from input stream
   *
   * @return msg received
   * @throws IOException if readline throws exception
   */
  public String recvMessage() throws IOException {
    return in.readLine();
  }
}
