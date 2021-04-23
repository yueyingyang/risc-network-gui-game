package edu.duke.ece651.risc.shared;

import java.awt.*;
import java.net.Socket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.connection.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ServerPlayer: Used on the server-side game
 * <p>
 * Could add more features based on server-side needs
 */
@JsonIgnoreProperties({ "in", "out", "clientSocket","currentGameID","color"})
public class ServerPlayer extends Player {
  private Socket clientSocket;
  private Integer currentGameID;
  private Color color;

  /**
   * @return the color of the player
   */
  public Color getColor() {
    return color;
  }

  /**
   * set color of the player
   * @param color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  public ServerPlayer(){
    super(null,null);
    this.clientSocket=null;
  }

  /**
   * the constructor of the player
   * @param in
   * @param out
   * @param cs
   */
  public ServerPlayer(BufferedReader in, PrintWriter out, Socket cs) {
    super(in, out);
    this.clientSocket = cs;
  }

  /**
   * set the players current gameID
   * @param gameID
   */
  public synchronized void setCurrentGameID(Integer gameID){
      this.currentGameID = gameID; 
  }

  /**
   * 
   * @return the player's gameID
   */
  public Integer getCurrentGame(){
    return this.currentGameID;
  }

  /**
   * read an action type from player
   *
   * @return action string
   * @throws IOException if R/W exception
   */
  public String readActionType() throws IOException {
    this.sendMessage(Constant.ASK_START_NEW_OR_JOIN);
    String s = this.recvMessage();
    // if it's neither j nor s, re-read
    while (!s.equals("j") && !s.equals("s")) {
      this.sendMessage(Constant.NOR_JOIN_START);
      s = this.recvMessage();
    }
    this.sendMessage(Constant.SUCCESS_ACTION_CHOOSED);
    return s;
  }

  /**
   * Read the size of game(i.e. number of players)
   *
   * @return size of game (limited from 2 to 5, inclusive)
   * @throws IOException if R/W exception
   */
  public int readGameSize() throws IOException {
    this.sendMessage(Constant.ASK_HOW_MANY_PLAYERS);
    int playerNum;
    while (true) {
      try {
        String s = this.recvMessage();
        playerNum = Integer.parseInt(s);
        if (playerNum < 2 || playerNum > 5) {
          throw new IllegalArgumentException(Constant.INVALID_NUMBER);
        }
        break;
      } catch (IllegalArgumentException e) {
        // re-read if illegal
        this.sendMessage(Constant.INVALID_NUMBER);
      }
    }
    this.sendMessage(Constant.SUCCESS_NUMBER_CHOOSED);
    return playerNum;
  }

  /**
   * Close player's I/O stream
   */
  public void closeSocket() {
    try {
      this.closeIOStream();
      this.clientSocket.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * change client socket
   * @param clientSocket
   */
  public void setSocket(Socket clientSocket){
    this.clientSocket = clientSocket;
  }
}
