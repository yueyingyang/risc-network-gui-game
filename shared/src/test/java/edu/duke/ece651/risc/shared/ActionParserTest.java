package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActionParserTest {
  ActionParser p;

  @BeforeEach
  void init() {
    p = new ActionParser();
  }

  @Test
  void test_make_place_entry() {
    assertThat(p.makePlaceEntry("1 2"), instanceOf(PlaceEntry.class));
    assertThrows(IllegalArgumentException.class, () -> p.makePlaceEntry("1"));
    assertThrows(IllegalArgumentException.class, () -> p.makePlaceEntry("1 a"));
  }

  @Test
  void test_make_attack_entry() {
    assertThat(p.makeAttackEntry("1 2 1"), instanceOf(AttackEntry.class));
    assertThrows(IllegalArgumentException.class, () -> p.makeAttackEntry("1 2 a"));
    assertThrows(IllegalArgumentException.class, () -> p.makeAttackEntry("1 a"));
  }

  @Test
  void test_make_move_entry() {
    assertThat(p.makeMoveEntry("1 2 1"), instanceOf(MoveEntry.class));
    assertThrows(IllegalArgumentException.class, () -> p.makeMoveEntry("1 2 a"));
    assertThrows(IllegalArgumentException.class, () -> p.makeMoveEntry("1 a"));
  }
}