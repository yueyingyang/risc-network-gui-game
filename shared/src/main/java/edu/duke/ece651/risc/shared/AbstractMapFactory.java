package edu.duke.ece651.risc.shared;

public interface AbstractMapFactory{
  /**
  @param 
   */
  List<Territory> getPlayerTerritory(String playerName);

  /**
  @param 
   */
  Territory getTerritoryByName(String name);

}