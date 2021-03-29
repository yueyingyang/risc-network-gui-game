package edu.duke.ece651.risc.server;
import com.ctc.wstx.shaded.msv_core.verifier.jarv.Const;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.*;
import java.io.*;
import java.util.*;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;


public class GameTest {
  @Test
  public void test_addPlayer() {
    Socket s = new Socket();
    Socket s1 = new Socket();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),s);
    Game g = new Game(1,0);
    assertNull(g.addPlayer(p));
    ServerPlayer p1 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),s1);
    assertEquals("This game is full, please select another game from the available list.", g.addPlayer(p1));
  }

  @Test
  public void test_isGameFull() {
    Socket s = new Socket();
    Game g = new Game(1,0);
    assertEquals(false, g.isGameFull());
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),s);
    g.addPlayer(p);
    assertEquals(true, g.isGameFull());
  }

  @Test
  public void test_getPlayerNum() {
    Game g = new Game(1,0);
    assertEquals(1, g.getPlayerNum());
  }

  @Test
  public void test_getPlayerByGame(){
    Game g = new Game(1,0);
    assertEquals(null,g.getPlayerByName("Red"));
  }

  @Test
  public void test_getPlayerInGameNum(){
    Game g = new Game(1,0);
    assertEquals(0,g.getPLayerInGameNum());
  }

  @Test
  public void test_makeMap() {
    Socket s = new Socket();
    Game g = new Game(1,0);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),s);
    g.addPlayer(p);
    g.makeMap(3);
    assertEquals(3,g.getMap().getAllTerritories().size());
  }

  @Test
  public void test_assignTerritories(){
    Socket s = new Socket();
    Game g = new Game(1,0);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),s);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(3);
    for(Territory t : g.getMap().getAllTerritories()){
      assertEquals("Red", t.getOwnerName());
    }
  }

  @Test
  public void test_sendObjectToAll() {
    Socket socket = new Socket();
    Game g = new Game(1,0);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(3);
    ArrayList<ServerPlayer> list = new ArrayList<>();
    list.add(p);
    g.sendObjectToAll(g.getMap(),list);
    JSONSerializer js = new JSONSerializer();
    String s = js.serialize(g.getMap());
    assertEquals(s+"\n", bytes.toString());
    GameMap map =(GameMap)js.deserialize(s,GameMap.class);
    assertEquals(3, map.getAllTerritories().size());
  }

  @Test
  public void test_sendStringToAll(){
    Socket socket = new Socket();
    Game g = new Game(1,0);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    ArrayList<ServerPlayer> list = new ArrayList<>();
    list.add(p);
    g.sendStringToAll("Hi", list);
    assertEquals("Hi\n", bytes.toString());
  }

  @Test
  public void test_placementPhase() throws IOException{
    Socket socket = new Socket();
    Game g = new Game(1,0); 
    ActionEntry p0 = new PlaceEntry("0",4, "");
    ActionEntry p1 = new PlaceEntry("1",4, "");
    Collection<ActionEntry> ca = new ArrayList<ActionEntry>();
    ca.add(p0);
    ca.add(p1); 
    JSONSerializer s = new JSONSerializer();
    String listJSON = s.getOm().writerFor(new TypeReference<List<ActionEntry>>() {}).writeValueAsString(ca);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader(listJSON)), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(2);
    g.placementPhase();
    for(Territory t:g.getMap().getPlayerTerritories("Red")){
      assertEquals(4,t.getNumSoldiersInArmy());
    }
  }

  @Test
  public void test_doAttacks(){
    Socket socket = new Socket();
    Game g = new Game(1,0);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(3);
    String res = g.doAttacks();
    String expected = "The combat results are:\n";
    assertEquals(expected,res);
  }

  @Test
  public void test_addSoldiersToAll(){
    Socket socket = new Socket();
    Game g = new Game(1,0);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(3);
    for(Territory t:g.getMap().getAllTerritories()){
      BasicArmy myArmy = new BasicArmy("Red",3);
      t.setMyArmy(myArmy);
    }
    g.addSoldiersToAll();
    int soldierNum = g.getMap().getAllTerritories().iterator().next().getNumSoldiersInArmy();
    assertEquals(4, soldierNum);
  }

  @Test
  public void test_checkWin(){
    Socket socket = new Socket();
    Game g = new Game(1,0);        
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes, true),socket);
    p.setName("Red");
    g.addPlayer(p);
    g.makeMap(3);
    assertEquals(true, g.checkWin());
    assertEquals(false, g.checkLost(p));
  }

  @Test
  public void test_checkLost(){
    Socket socket1 = new Socket();
    Socket socket2 = new Socket();
    Game g = new Game(2,0);        
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();  
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p1 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes1, true),socket1);
    p1.setName("Red");
    ServerPlayer p2 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes2, true),socket2);
    p2.setName("Blue");
    g.addPlayer(p1);
    g.addPlayer(p2);
    g.makeMap(3);
    assertEquals(false, g.checkWin());
    for(Territory t:g.getMap().getAllTerritories()){
      t.setOwnerName("Red");
    }    
    assertEquals(true, g.checkLost(p2));
  }

  @Test
  public void test_endGame() throws InterruptedException, IOException{
    Socket socket1 = new Socket();
    Socket socket2 = new Socket();
    Game g = new Game(2,0);        
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();  
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p1 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes1, true),socket1);
    p1.setName("Red");
    p1.setCurrentGameID(0);
    ServerPlayer p2 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes2, true),socket2);
    p2.setName("Blue");
    p2.setCurrentGameID(0);
    g.addPlayer(p1);
    g.addPlayer(p2);
    Thread t = new Thread(() -> {
      try {
        g.runGame(2,6);
      } catch (Exception ignored) {
      }
    });
    t.start();
    // wait for "acceptPlayers" finishing
    Thread.sleep(2000);
    g.endGame();
    assertEquals(2,p2.getCurrentGame());
    assertThrows(Exception.class, ()->{p2.recvMessage();});   
  }

  @Test
  public void test_updatePlayerLists() throws IOException, InterruptedException{
    PlaceEntry pe1 = new PlaceEntry("0", 2, "Red");
    PlaceEntry pe2 = new PlaceEntry("1", 0, "Blue");
    JSONSerializer js = new JSONSerializer();
    String placement1 =js.serialize(pe1);
    String placement2 =js.serialize(pe2);
    Socket socket1 = new Socket();
    Socket socket2 = new Socket();
    Game g = new Game(2,0);        
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();  
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();                                                                                                   
    ServerPlayer p1 = new ServerPlayer(new BufferedReader(new StringReader(placement1)), new PrintWriter(bytes1, true),socket1);
    p1.setName("Red");
    p1.setCurrentGameID(0);
    ServerPlayer p2 = new ServerPlayer(new BufferedReader(new StringReader(placement2)), new PrintWriter(bytes2, true),socket2);
    p2.setName("Blue");
    p2.setCurrentGameID(0);
    g.addPlayer(p1);
    g.addPlayer(p2);
    Thread t = new Thread(() -> {
      try {
        g.runGame(1,2);
      } catch (Exception ignored) {
      }
    });
    t.start();
    // wait for "acceptPlayers" finishing
    Thread.sleep(5000);  
    assertEquals(2,g.getMap().getTerritory("0").getNumSoldiersInArmy());
    assertEquals(2,g.getMap().getTerritory("1").getNumSoldiersInArmy());
    AttackEntry a = new AttackEntry("0", "1", 2, "Red");
    a.apply(g.getMap());
    g.doAttacks();
    assertEquals("Red", g.getMap().getTerritory("1").getOwnerName());   
    assertEquals(true,g.checkLost(p2));
    g.updatePlayerLists();
    //assertThrows(Exception.class,()->{p1.recvMessage();});
    //assertThrows(Exception.class,()->{p2.recvMessage();});

  }

  

}
