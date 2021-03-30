package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.g;
import org.junit.jupiter.api.Test;

public class GameThreadTest {
  @Test
  public void test_gameThreadConstructor() {
    Game g = new Game(1,0);
    GameThread gt = new GameThread(g);
    assertDoesNotThrow(()->{gt.start();});
  }

}











