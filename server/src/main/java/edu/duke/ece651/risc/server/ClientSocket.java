package edu.duke.ece651.risc.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This is a data struct that holds info of a single client
 */
public class ClientSocket {
  /**
   * The outputStream of the connection
   */
  private final PrintWriter out;
  /**
   * The inputStream of the connection
   */
  private final BufferedReader in;

  /**
   * The constructor of the class
   * @param in
   * @param out
   */
  public ClientSocket(BufferedReader in, PrintWriter out){
    this.in = in;
    this.out = out;
  }

  /**
   * 
   * @return the inputStream
   */
  public BufferedReader getInput(){
    return this.in;
  }

  /**
   * 
   * @return the outputStream
   */
  public PrintWriter getOutput(){
    return this.out;
  }

  
}











