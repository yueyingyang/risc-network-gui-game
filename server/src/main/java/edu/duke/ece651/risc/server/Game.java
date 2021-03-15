package edu.duke.ece651.risc.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.Action;

import java.io.*;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.*;
import java.util.Collection;

/**
 * Game class is responsible for the one game's play
 */
public class Game {

  /**
   * all players in the game
   */
  private final int playerNum;
  private final ArrayList<Player> players;
  private final HashSet<String> colorSet;
  private GameMap gameMap;
  private Checker myChecker;
  private JSONSerializer js;
  private Random myRandom;

  /**
   * the construtor of the game
   *
   * @param playerNum is the number of players in this game
   */
  public Game(int playerNum) {
    this.playerNum = playerNum;
    this.players = new ArrayList<>();
    this.colorSet = new HashSet<>();
    makeColors();
    this.myChecker = null;
    this.js = new JSONSerializer();
    this.myRandom = new Random(1);
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
   * false if can accpet more players
   */
  public Boolean isGameFull() {
    return (this.players.size() >= this.playerNum);
  }

  /**
   * try to add one player to the game
   *
   * @return null if the player is successfully added into the game
   */
  public String addPlayer(Player player) {
    if (this.isGameFull()) {
      // error information
      return "This game is full, please select another game from the available list.";
    }
    // add successfully
    String playerColor = colorSet.iterator().next();
    player.setName(playerColor);
    colorSet.remove(playerColor);
    this.players.add(player);
    return null;
  }

  /**
   * @return the number of players that can start a game
   */
  public int getPlayerNum() {
    return playerNum;
  }


  /**
   * create the game map when all players are added into the game, i.e the game room is full
   * @param territoriesPerPlayer
   * @return
   */
  public void makeMap(int territoriesPerPlayer){
    V1MapFactory factory = new V1MapFactory();
    ArrayList<String> nameList = new ArrayList<String>();
    for(Player player:players){
      nameList.add(player.getName());
    }
    this.gameMap = factory.createMap(nameList, territoriesPerPlayer);
  }

  public GameMap getMap(){
    return this.gameMap;
  }

  /**
   * after creating the map, assign territorities to players
   * each player will have territoriesPerPlayer territories
   * @param territoriesPerPlayer
   */
  public void assignTerritories(int territoriesPerPlayer){
    ArrayList<Territory> territories = this.getMap().getAllTerritories();
    for(int i=0;i<players.size();i++){
      String playerName = players.get(i).getName();
      for(int j=0;j<territoriesPerPlayer;j++){
        territories.get(i*territoriesPerPlayer+j).setOwnerName(playerName);
      }
    }
  }

  /**
   * send object to one player
   * @param o the object to send
   * @param p the player to send to
   */
  public void sendToOne(Object o, Player p){
    p.sendMessage(js.serialize(o));
    System.out.println(js.serialize(o));
  }

  /**
   * send object to all players in the game
   * @param o the object to send
   */
  public void sendToAll(Object o){
    for(Player player:players){
      sendToOne(o,player);
    }
  }
  

  /**
   * place soldiers on the map according to the all players' input
   * @param soldierNum the number of soldiers one player has in total
   */
  public void placementPhase() throws IOException{
    for(Player player:players){
      String s = player.recvMessage();
      Collection<ActionEntry> placements = js.getOm().readValue(s, new TypeReference<Collection<ActionEntry>>() {});
      for(ActionEntry placement : placements){
        placement.apply(gameMap, myChecker);
      }
    }
  }


  public void playOneTurn() throws IOException{
    for(Player player:players){
      //server send map to all players despite the player is lost or not
      sendToOne(gameMap, player);
      //if one player is lost, should not send anything to the server
      if(checkLost(player)==false){
        while(true){
          String s = player.recvMessage();
          //check if the player has done with his orders
          if(s.equals(Constant.ORDER_COMMIT)){break;}
          ActionEntry action = (ActionEntry)js.deserialize(s, ActionEntry.class);

          //need to catch exception here if fails
          action.apply(gameMap, myChecker);
          sendToOne(Constant.VALID_ACTION, player);
        }
      }  
    }
    for(Territory t : gameMap.getAllTerritories()){
      t.resolveCombat(myRandom);
    } 
    for(Player player:players){
      if(checkLost(player)==true){sendToOne(Constant.LOSE_GAME,player);}
      else{sendToOne(Constant.CONTINUE_PLAYING, player);}
    }    
    //add 1 soldier to all territories
    for(Territory t:gameMap.getAllTerritories()){
      t.addSoldiersToArmy(1);
    }
  }


  /**
   * check whether a particular player has lost all his/her territories
   * @param player
   * @return true if is lost
   * @return false if still has territories
   */
  Boolean checkLost(Player player){
    if(((ArrayList<Territory>)gameMap.getPlayerTerritories(player.getName())).size()==0){
      return true;
    }
    return false;
  }

  /**
   * 
   * @return true if the game is over, the winner has been raised
   * @return false if still needs more fights
   */
  Boolean checkWin(){
    if(this.gameMap.getAllPlayerTerritories().size()==1){
      return true;
    }
    return false;
  }

/**
 * after the game room's required number of people is reached, run the game
 * @throws IOException
 */
  public void runGame()throws IOException{
    if(players.size()==playerNum){
      int TerritoryPerPlayer = 3;//assume that one player has three territories
      int totalSoldiers = 12;//assume that each player have 12 soldiers in total 
      makeMap(TerritoryPerPlayer);
      assignTerritories(TerritoryPerPlayer);
      sendToAll(this.gameMap);
      sendToAll(String.valueOf(totalSoldiers));
      placementPhase();
      while(true){
        this.playOneTurn();
        if(checkWin()==true){
          String winner = this.gameMap.getAllPlayerTerritories().keySet().iterator().next();
          sendToAll("The winner is "+winner);
          sendToAll(Constant.GAME_OVER);
          break;
        }
      }
    }
  }
  
}


