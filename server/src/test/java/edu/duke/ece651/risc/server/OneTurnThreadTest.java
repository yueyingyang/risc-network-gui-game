package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.Army;
import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.JSONSerializer;
import edu.duke.ece651.risc.shared.entry.MoveEntry;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.ServerPlayer;
import edu.duke.ece651.risc.shared.Territory;

class OneTurnThreadTest {
  @Test
  public void test_oneTurnExceoption() throws InterruptedException, BrokenBarrierException{

    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    Territory t1=new Territory("A");
    Territory t2=new Territory("B");
    Territory t3=new Territory("C");
    Territory t4=new Territory("D");
    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");
    t3.setOwnerName("Red");
    t4.setOwnerName("Red");

    t1.setMyArmy(new Army(t1.getOwnerName(), 1));
    t2.setMyArmy(new Army(t2.getOwnerName(), 1));
    t3.setMyArmy(new Army(t3.getOwnerName(), 1));
    t4.setMyArmy(new Army(t4.getOwnerName(), 1));

    territoryFinder.put("A",t1);
    territoryFinder.put("B",t2);
    territoryFinder.put("C",t3);
    territoryFinder.put("D",t4);


    GameMap map=new GameMap(connections,territoryFinder);
    ActionEntry a = new MoveEntry("A", "B", 1, "Blue");
    JSONSerializer js = new JSONSerializer();
    String sa = js.serialize(a);
    Socket s = new Socket();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader in = new BufferedReader(new StringReader(sa+"\n"+Constant.ORDER_COMMIT+"\n"));
    ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
//    work should be done during adding player
    p.setName("Blue");
    p.setColor(Color.red);
    p.setCurrentGameID(0);
    PlayerInfo pi = new PlayerInfo("Blue");
    CyclicBarrier barrier = new CyclicBarrier(2);
    OneTurnThread t = new OneTurnThread(map,p, new ArrayList<>(Collections.singletonList(p)),pi,barrier,0);
    Thread newthread = new Thread(t);
    assertDoesNotThrow(()->{newthread.start();barrier.await();});
    assertEquals("Blue",t1.getOwnerName());
    assertEquals(1, t1.getNumSoldiersInArmy());
    assertEquals(1, t2.getNumSoldiersInArmy());
  }

  @Test
  public void test_oneTurnNoException() throws InterruptedException, BrokenBarrierException{
    List<List<String>> connections=new ArrayList<>();
    Map<String,Territory> territoryFinder=new HashMap<>();
    Territory t1=new Territory("A");
    Territory t2=new Territory("B");
    t1.setOwnerName("Blue");
    t2.setOwnerName("Blue");

    t1.setMyArmy(new Army(t1.getOwnerName(), 1));
    t2.setMyArmy(new Army(t2.getOwnerName(), 1));

    territoryFinder.put("A",t1);
    territoryFinder.put("B",t2);

    List<String> connection1=new ArrayList<>();
    connection1.add("A");
    connection1.add("B");
    connections.add(connection1);

    GameMap map=new GameMap(connections,territoryFinder);
    ActionEntry a = new MoveEntry("A", "B", 1, "Blue");
    JSONSerializer js = new JSONSerializer();
    String sa = js.serialize(a);
    Socket s = new Socket();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader in = new BufferedReader(new StringReader(sa+"\n"+Constant.ORDER_COMMIT+"\n"));
    ServerPlayer p = new ServerPlayer(in, new PrintWriter(bytes, true),s);
    //    work should be done during adding player
    p.setName("Blue");
    p.setColor(Color.BLUE);
    p.setCurrentGameID(0);
    PlayerInfo pi = new PlayerInfo("Blue");
    CyclicBarrier barrier = new CyclicBarrier(2);
    Map<String,String> playerColorMap = new HashMap<String,String>();
    OneTurnThread t = new OneTurnThread(map,p,new ArrayList<>(Collections.singletonList(p)),pi,barrier,0,playerColorMap);
    Thread newthread = new Thread(t);
    assertDoesNotThrow(()->{newthread.start();barrier.await();});  
    assertEquals("Blue",t1.getOwnerName());
    assertEquals(0, t1.getNumSoldiersInArmy());
    assertEquals(2, t2.getNumSoldiersInArmy());
  }

}










