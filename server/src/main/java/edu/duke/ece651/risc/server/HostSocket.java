package edu.duke.ece651.risc.server;
import java.net.*;
import java.util.ArrayList;

import java.io.*;

/**
 * The HostSocket class for the gameServer
 */
public class HostSocket {
  private final int portNumber;
  private final int playerNum;
  
  /**
   * the constructor that takes a portNumber and a playerNum
   * @param portNumber
   * @param playerNum
   */
  public HostSocket(int portNumber,int playerNum){
    this.portNumber = portNumber;
    this.playerNum = playerNum;
  }

  /**
   * build server and connect with players. If success, 
   * will print whatever the player send and then send player a success message
   * @return the list of players' ClientSocket 
   */
  ArrayList<ClientSocket> waitForConnections(){
    int num = 0;
    ArrayList<ClientSocket> clients = new ArrayList<ClientSocket>(); 
    while(num<this.playerNum){
      try( ServerSocket serverSocket =
           new ServerSocket(this.portNumber);
           Socket clientSocket = serverSocket.accept();     
           PrintWriter out =
           new PrintWriter(clientSocket.getOutputStream(), true);                   
           BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
        {
        num++;
        ClientSocket client = new ClientSocket(in,out);
        clients.add(client);
        String inputLine;
        /*while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }*/
       inputLine = in.readLine();
        System.out.println(inputLine);
        out.println("Successfully recv message from player!");
      }catch (IOException e){
        System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
        System.out.println(e.getMessage());
      }
    }
    return clients;
  }

  
}






