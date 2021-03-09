package edu.duke.ece651.risc.server;

import java.util.ArrayList;

import edu.duke.ece651.risc.shared.Player;

/**
 * Game class is responsible for the one game's play
 */
public class Game {
  /**
   * the server socket of the game
   */
  private HostSocket hostSocket;
  /**
   * all players in the game
   */
  private ArrayList<Player> players;

  /**
   * the construtor of the game
   * @param hostSocket, the server socket that all players will need to connect
   */
  public Game(HostSocket hostSocket){
    this.hostSocket = hostSocket;
    this.players = hostSocket.waitForConnections();
  }

  /**
   * 
   * @return the number of players in the game
   */
  public int getPlayerNum(){
    return players.size();
  }

  /**
   * 
   * @return the server socket of the game
   */
  public HostSocket getHostSocket(){
    return this.hostSocket;
  }

}











