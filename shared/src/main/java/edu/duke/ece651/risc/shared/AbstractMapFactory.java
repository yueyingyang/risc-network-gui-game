package edu.duke.ece651.risc.shared;
import java.util.*;

public interface AbstractMapFactory{


  /**
  @param create 
   */
  public GameMap createV1Map(List<String> nameList, int territoriesPerPlayer);
  

}