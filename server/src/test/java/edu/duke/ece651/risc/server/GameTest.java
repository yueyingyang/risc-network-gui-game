package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.ServerPlayer;

public class GameTest {
  @Test
  public void test_addPlayer() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Player p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes,true));
    Game g = new Game(1);
    assertEquals("", g.addPlayer(p));
    Player p1 = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes,true));
    assertEquals("This game is full, please select another game from the available list.",g.addPlayer(p1));
  }

  @Test
  public void test_isGameFull(){
    Game g = new Game(1);
    assertEquals(false,g.isGameFull());
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Player p = new ServerPlayer(new BufferedReader(new StringReader("")), new PrintWriter(bytes,true));
    g.addPlayer(p);
    assertEquals(true,g.isGameFull());
  }

  @Test
  public void test_getPlayerNum() {
    Game g = new Game(1);
    assertEquals(1,g.getPlayerNum());
  }

}

