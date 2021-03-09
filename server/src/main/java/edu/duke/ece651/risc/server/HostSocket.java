package edu.duke.ece651.risc.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.ServerPlayer;

/**
 * The HostSocket class for the gameServer
 */
public class HostSocket {
  private ServerSocket serverSocket;
  private final int portNumber;
  private final int playerNum;
  
  /**
   * the constructor that takes a portNumber and a playerNum
   * make the server socket listen on portNum
   * @param portNumber
   * @param playerNum
   */
  public HostSocket(int portNumber,int playerNum){
    this.portNumber = portNumber;
    this.playerNum = playerNum;
    try{
      this.serverSocket = new ServerSocket(this.portNumber); 
    }catch(IOException e){
      System.out.println("Exception caught when listening fon port"+portNumber);
      System.out.println(e.getMessage());
    }     
  }

  /**
   * close the server socket
   */
  public void closeSocket(){
    try{
       this.serverSocket.close();
     } catch (IOException e) {
      System.out.println("Exception caught when closing the server socket.");
      System.out.println(e.getMessage());
    }
    
  }

  /**
   * server connect with players. If success, 
   * will print whatever the player send and then send player a success message
   * @return the list of players' ClientSocket 
   */
  ArrayList<Player> waitForConnections(){
    int num = 0;
    ArrayList<Player> players = new ArrayList<Player>(); 
    while(num<this.playerNum){
      try{
        Socket clientSocket = serverSocket.accept();     
        PrintWriter out =
        new PrintWriter(clientSocket.getOutputStream(), true);                   
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Player player = new ServerPlayer(in,out);
        players.add(player);
        String inputLine;
        /*while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }*/
        out.println(num);
        inputLine = in.readLine();
        System.out.println(inputLine);
        out.println("Successfully connect to the server!");
        num++;
      }catch (IOException e){
        System.out.println("Exception caught when listening for a connection");
        System.out.println(e.getMessage());
      }
    }
    return players;
  }
  
}






