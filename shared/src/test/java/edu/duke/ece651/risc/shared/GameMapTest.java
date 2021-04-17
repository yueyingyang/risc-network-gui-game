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
    assertTrue(map.getTerritory("D")==null);
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

  @Test
  public void test_getPlayerTerritories(){
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    Territory t1=new Territory("A");
    Territory t2=new Territory("B");
    Territory t3=new Territory("C");
    Territory t4=new Territory("D");
    territoryFinder.put("A",t1);
    territoryFinder.put("B",t2);
    territoryFinder.put("C",t3);
    territoryFinder.put("D",t4);

    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");
    t3.setOwnerName("Red");
    t4.setOwnerName("Red");

    GameMap map=new GameMap(connections,territoryFinder);
    Set<Territory> expected=new HashSet<>();
    expected.add(t1);
    expected.add(t2);
    int size=0;
    for(Territory t:map.getPlayerTerritories("Blue")){
      assertTrue(expected.contains(t));
      size++;
    }
    assertEquals(expected.size(),size);
  }
  
  @Test
  public void test_getAllPlayerTerritories(){
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    Territory t1=new Territory("A");
    Territory t2=new Territory("B");
    Territory t3=new Territory("C");
    Territory t4=new Territory("D");
    territoryFinder.put("A",t1);
    territoryFinder.put("B",t2);
    territoryFinder.put("C",t3);
    territoryFinder.put("D",t4);

    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");
    t3.setOwnerName("Red");
    t4.setOwnerName("Red");

    GameMap map=new GameMap(connections,territoryFinder);
    Map<String,Set<Territory>> playerMap=map.getAllPlayerTerritories();
    assertTrue(playerMap.containsKey("Blue"));
    assertTrue(playerMap.containsKey("Red"));
    assertTrue(playerMap.get("Blue").contains(t1));
    assertTrue(playerMap.get("Blue").contains(t2));
    assertTrue(playerMap.get("Red").contains(t3));
    assertTrue(playerMap.get("Red").contains(t4));
    assertEquals(playerMap.get("Blue").size(),2);
    assertEquals(playerMap.get("Red").size(),2);
  }

  @Test
  public void test_getAllTerritories(){
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    Territory t1=new Territory("A");
    Territory t2=new Territory("B");
    Territory t3=new Territory("C");
    Territory t4=new Territory("D");
    territoryFinder.put("A",t1);
    territoryFinder.put("B",t2);
    territoryFinder.put("C",t3);
    territoryFinder.put("D",t4);

    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");
    t3.setOwnerName("Red");
    t4.setOwnerName("Red");

    GameMap map=new GameMap(connections,territoryFinder);
    ArrayList<Territory> allTerritories = map.getAllTerritories();
    assertTrue(allTerritories.contains(t1));
    assertTrue(allTerritories.contains(t2));
    assertTrue(allTerritories.contains(t3));
    assertTrue(allTerritories.contains(t4));
  }


  @Test
  public void test_computeCost(){
    Territory t1=new Territory("1",3,0,0);
    Territory t2=new Territory("2",3,0,0);
    Territory t3=new Territory("3",3,0,0);
    Territory t4=new Territory("4",1,0,0);
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    territoryFinder.put("1",t1);
    territoryFinder.put("2",t2);
    territoryFinder.put("3",t3);
    territoryFinder.put("4",t4);
    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");
    t3.setOwnerName("Blue");
    t4.setOwnerName("Red");

    List<String> connection1=new ArrayList<>();
    connection1.add("1");
    connection1.add("2");
    List<String> connection2=new ArrayList<>();
    connection2.add("2");
    connection2.add("3");
    List<String> connection3=new ArrayList<>();
    connection3.add("3");
    connection3.add("4");
    List<String> connection4=new ArrayList<>();
    connection4.add("4");
    connection4.add("1");
    connections.add(connection1);
    connections.add(connection2);
    connections.add(connection3);
    connections.add(connection4);
    GameMap map=new GameMap(connections,territoryFinder);
    int cost=map.computeCost(t1,t3,2);
    assertEquals(cost,18);
  }

  @Test
  public void test_isAdjacentEnemy(){
    V2MapFactory factory=new V2MapFactory();
    List<String> nameList=new ArrayList<>();
    nameList.add("Red");
    nameList.add("Blue");
    nameList.add("Green");
    GameMap map=factory.createMap(nameList,2);

    assertTrue(map.isAdjacentEnemy("Red",map.getTerritory("2")));
    assertFalse(map.isAdjacentEnemy("Red",map.getTerritory("4")));
    assertTrue(map.isAdjacentEnemy("Red",map.getTerritory("5")));
    assertTrue(map.isAdjacentEnemy("Red",map.getTerritory("3")));
    assertTrue(map.isAdjacentEnemy("Red",map.getTerritory("0")));
  }
}
