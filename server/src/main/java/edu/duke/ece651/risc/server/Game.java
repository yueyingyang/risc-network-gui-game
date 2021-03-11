package edu.duke.ece651.risc.server;

import java.util.ArrayList;
import java.util.HashSet;

import edu.duke.ece651.risc.shared.Player;

/**
 * Game class is responsible for the one game's play
 */
public class Game {

  /**
   * all players in the game
   */
  private int playerNum;
  private ArrayList<Player> players;
  private HashSet<String> colorSet;

  /**
   * the construtor of the game
   * 
   * @param hostSocket the server socket that all players will need to connect
   */
  public Game(int playerNum) {
    this.playerNum = playerNum;
    this.players = new ArrayList<Player>();
    this.colorSet = new HashSet<String>();
    makeColors();
  }

  /**
   * make a color set at the beginning of the game
   */
  private void makeColors() {
    this.colorSet.add("Red");
    this.colorSet.add("Orange");
    this.colorSet.add("Green");
    this.colorSet.add("Blue");
    this.colorSet.add("Purple");
  }

  /**
   * check if the game's player number limit has been reached
   * 
   * @return true if no more players can be accepted
   * @return false if can accpet more players
   */
  public Boolean isGameFull() {
    return (this.players.size() >= this.playerNum);
  }

  /**
   * try to add one player to the game
   * 
   * @return a helper message if the player cannot be accpeted into the game
   * @return null if the player is successfully added into the game
   */
  public String addPlayer(Player player) {
    if (this.isGameFull() == false) {
      String playerColor = colorSet.iterator().next();
      player.setName(playerColor);
      colorSet.remove(playerColor);
      player.sendMessage(playerColor);
      this.players.add(player);
      return "";
    } else {
      String s = "This game is full, please select another game from the available list.";
      return s;
    }
  }

  /**
   * 
   * @return the number of players in the game
   */
  public int getPlayerNum() {
    return playerNum;
  }

}
