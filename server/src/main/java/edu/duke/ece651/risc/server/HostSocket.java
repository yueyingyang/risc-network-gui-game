package edu.duke.ece651.risc.server;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * The HostSocket class for the gameServer
 */
public class HostSocket {
  private ServerSocket serverSocket;
  private final int portNumber;

  /**
   * the constructor that takes a portNumber and a playerNum make the server
   * socket listen on portNum
   * 
   * @param portNumber
   */
  public HostSocket(int portNumber) {
    this.portNumber = portNumber;
    try {
      this.serverSocket = new ServerSocket(this.portNumber);
    } catch (IOException e) {
      System.out.println("Exception caught when listening fon port" + portNumber);
      System.out.println(e.getMessage());
    }
  }


  /**
   * close the server socket
   */
  public void closeSocket() {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      System.out.println("Exception caught when closing the server socket.");
      System.out.println(e.getMessage());
    }
  }

  public ServerSocket getSocket(){
    return this.serverSocket;
  }

}
