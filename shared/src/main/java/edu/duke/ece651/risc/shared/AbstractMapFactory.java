package edu.duke.ece651.risc.shared;
import java.util.*;

public interface AbstractMapFactory{


  /**
  *create a game map object for version1's game
  @param nameList player's name list
  @param territoriesPerPlayer how many territories each player have
  @return a game map
   */
  public GameMap createMap(List<String> nameList, int territoriesPerPlayer);
  

}