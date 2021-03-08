package edu.duke.ece651.risc.server;

import java.util.ArrayList;

import edu.duke.ece651.risc.shared.Player;

public class Game {
  private HostSocket hostSocket;
  private ArrayList<Player> players;

  public Game(HostSocket hostSocket){
    this.hostSocket = hostSocket;
    this.players = hostSocket.waitForConnections();
  }

  public int getPlayerNum(){
    return players.size();
  }

  public HostSocket getHostSocket(){
    return this.hostSocket;
  }

}











