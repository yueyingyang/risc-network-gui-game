package edu.duke.ece651.risc.shared.game;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {
  @Test
  void test_equals() {
    GameInfo gi1 = new GameInfo(0, Arrays.asList("p1", "p2"));
    GameInfo gi2 = new GameInfo(0, Arrays.asList("p1", "p2"));
    assertEquals(gi1, gi2);
    GameInfo gi3 = new GameInfo(0, Arrays.asList("p3", "p4"));
    assertNotEquals(gi1, gi3);
    assertNotEquals(gi1, "2333");
  }
}