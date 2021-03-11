package edu.duke.ece651.risc.shared;

public interface AbstractMapFactory{
  /**
  @param 
   */
  Iterable<Territory> getPlayerTerritories(String playerName);

  /**
  @param 
   */
  Territory getTerritoryByName(String name);

}