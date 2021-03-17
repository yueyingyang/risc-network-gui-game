package edu.duke.ece651.risc.server;
import com.fasterxml.jackson.core.type.TypeReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;


public class GameTest {
  @Test
  public void test_addPlayer() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Player p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    Game g = new Game(1);
    assertNull(g.addPlayer(p));
    Player p1 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    assertEquals("This game is full, please select another game from the available list.", g.addPlayer(p1));
  }

  @Test
  public void test_isGameFull() {
    Game g = new Game(1);
    assertEquals(false, g.isGameFull());
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Player p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    g.addPlayer(p);
    assertEquals(true, g.isGameFull());
  }

  @Test
  public void test_getPlayerNum() {
    Game g = new Game(1);
    assertEquals(1, g.getPlayerNum());
  }

  @Test
  public void test_makeMap() {
    Game g = new Game(1);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    g.addPlayer(p);
    g.makeMap(3);
    assertEquals(3,g.getMap().getAllTerritories().size());
  }

  @Test
  public void test_assignTerritories(){
    Game g = new Game(1);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    g.addPlayer(p);
    g.makeMap(3);
    g.assignTerritories(3);
    for(Territory t : g.getMap().getAllTerritories()){
      assertEquals("Red", t.getOwnerName());
    }
  }

  @Test
  public void test_sendToAll() {
    Game g = new Game(1);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true));
    g.addPlayer(p);
    g.makeMap(3);
    g.assignTerritories(3);
    ArrayList<Player> list = new ArrayList<>();
    list.add(p);
    g.sendObjectToAll(g.getMap(),list);
    JSONSerializer js = new JSONSerializer();
    String s = js.serialize(g.getMap());
    assertEquals(s+"\n", bytes.toString());
    GameMap map =(GameMap)js.deserialize(s,GameMap.class);
    assertEquals(3, map.getAllTerritories().size());
  }

  @Test
  public void test_placementPhase() throws IOException{
    Game g = new Game(1); 
    ActionEntry p0 = new PlaceEntry("0",4);
    ActionEntry p1 = new PlaceEntry("1",4);
    Collection<ActionEntry> ca = new ArrayList<ActionEntry>();
    ca.add(p0);
    ca.add(p1); 
    JSONSerializer s = new JSONSerializer();
    String listJSON = s.getOm().writerFor(new TypeReference<List<ActionEntry>>() {}).writeValueAsString(ca);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader(listJSON)), new PrintWriter(bytes, true));
    g.addPlayer(p);
    g.makeMap(2);
    g.assignTerritories(2);
    g.placementPhase();
    for(Territory t:g.getMap().getPlayerTerritories("Red")){
      assertEquals(4,t.getNumSoldiersInArmy());
    }
  }

  @Test
  public void test_checkLost(){
    
  }

}
