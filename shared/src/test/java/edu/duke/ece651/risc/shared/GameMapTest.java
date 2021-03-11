package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

public class GameMapTest {
  @Test
  public void test_getTerritory(){
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    

    territoryFinder.put("A",new Territory("A"));
    territoryFinder.put("B",new Territory("B"));
    territoryFinder.put("C",new Territory("C"));

    GameMap map=new GameMap(connections,territoryFinder);
    Territory a=map.getTerritory("A");
    Territory b=map.getTerritory("B");
    Territory c=map.getTerritory("C");
    assertEquals(a.getName(),"A");
    assertEquals(b.getName(),"B");
    assertEquals(c.getName(),"C");
  }

  @Test
  public void test_setConnection(){
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    List<String> connection1=new ArrayList<>();
    connection1.add("A");
    connection1.add("B");
    List<String> connection2=new ArrayList<>();
    connection2.add("B");
    connection2.add("C");
    connections.add(connection1);
    connections.add(connection2);

    territoryFinder.put("A",new Territory("A"));
    territoryFinder.put("B",new Territory("B"));
    territoryFinder.put("C",new Territory("C"));

    GameMap map=new GameMap(connections,territoryFinder);
    Territory a=map.getTerritory("A");
    Territory b=map.getTerritory("B");
    Territory c=map.getTerritory("C");

    assertTrue(a.isAdjacent(b));
    assertTrue(b.isAdjacent(c));
    assertFalse(a.isAdjacent(c));
  }
  

}
