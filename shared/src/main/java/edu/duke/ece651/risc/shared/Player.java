package edu.duke.ece651.risc.shared;


import java.io.*;
import java.util.logging.Logger;

/**
 * This is the abstract player: support recv and send message
 */
public abstract class Player {
  private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
  // IO stream with game server
  protected BufferedReader in;
  public PrintWriter out;
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

  /**
   * get the player's name
   *
   * @return the player's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Write to output stream
   *
   * @param msg is the msg to write
   */
  public void sendMessage(String msg) {
    //LOGGER.info("[" + name + "]" + msg);
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
    //LOGGER.info("[" + name + "]" + rev);
    return rev;
  }

  /**
   * Write to the output stream
   *
   * @param o is the object to send
   */
  public void sendObject(Object o) {
    JSONSerializer js = new JSONSerializer();
    //LOGGER.info("[" + name + "]" + js.serialize(o));
    System.out.println(out.equals(null));
    out.println(js.serialize(o));
  }

  /**
   * Close IO stream
   *
   * @throws IOException if IOE
   */
  public void closeIOStream() throws IOException {
    in.close();
    out.close();
  }

  /**
   * @param in  is inputstream of the player
   * @param out is the outputstream of the player
   */
  public void setInOut(BufferedReader in, PrintWriter out) {
    this.in = in;
    this.out = out;
  }

}

