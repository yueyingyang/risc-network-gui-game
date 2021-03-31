package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JSONSerializerTest {
  private final JSONSerializer s = new JSONSerializer();

  @Test
  void test_serialize_and_de_map() {
    // prep a map and place units
    GameMap map = getGameMap();
    String result = s.serialize(map);
    GameMap deMap = (GameMap) s.deserialize(result, GameMap.class);
    assertEquals(map, deMap);
    assertDoesNotThrow(() -> new MapView(deMap).display());
  }

  private GameMap getGameMap() {
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    List<ActionEntry> placement = new ArrayList<>();
    placement.add(new PlaceEntry("0", 1, "player1"));
    placement.add(new PlaceEntry("1", 1, "player1"));
    placement.add(new PlaceEntry("2", 1, "player1"));
    placement.add(new PlaceEntry("3", 1, "player1"));
    for (ActionEntry pe : placement) {
      pe.apply(map);
    }
    return map;
  }

  @Test
  void test_serialize_and_de_action() throws JsonProcessingException {
    ActionEntry placeEntry = new PlaceEntry("0", 2, "player1");
    ActionEntry place1Entry = new PlaceEntry("1", 2, "player1");
    ActionEntry attackEntry = new AttackEntry("0", "3", 1, "player1");
    ActionEntry moveEntry = new MoveEntry("0", "1", 1, "player1");
    // test serializer
    ActionEntry dePlace = (ActionEntry) s.deserialize(s.serialize(placeEntry), ActionEntry.class);
    ActionEntry deAttack = (ActionEntry) s.deserialize(s.serialize(attackEntry), ActionEntry.class);
    ActionEntry deMove = (ActionEntry) s.deserialize(s.serialize(moveEntry), ActionEntry.class);
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    assertThat(dePlace, instanceOf(PlaceEntry.class));
    assertThat(deAttack, instanceOf(AttackEntry.class));
    assertThat(deMove, instanceOf(MoveEntry.class));

    dePlace.apply(map);
    place1Entry.apply(map);
    deAttack.apply(map);
    deMove.apply(map);
    assertDoesNotThrow(() -> new MapView(map).display());
  }

  @Test
  void test_serialize_and_de_placement_list() throws JsonProcessingException {
    List<ActionEntry> p = new ArrayList<>();
    p.add(new PlaceEntry("0", 2, "player1"));
    p.add(new PlaceEntry("1", 2, "player1"));
    p.add(new AttackEntry("0", "3", 1, "player1"));
    p.add(new MoveEntry("0", "1", 1, "player1"));
    // test serializer
    String listJSON = s.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(p);
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    Collection<ActionEntry> pd = s.getOm().readValue(listJSON, new TypeReference<>() {
    });
    for (ActionEntry a : pd) {
      a.apply(map);
    }
    assertDoesNotThrow(() -> new MapView(map).display());
  }

  @Test
  void test_exception() {
    PrintStream originalStream = System.err;
    PrintStream dummyStream = new PrintStream(new OutputStream() {
      public void write(int b) {
        // NO-OP
      }
    });
    System.setErr(dummyStream);
    assertDoesNotThrow(() -> s.deserialize("it's not a json string", String.class));
  }

  @Test
  void test_clone() {
    GameMap map = getGameMap();
    GameMap cloned = (GameMap) new JSONSerializer().clone(map, GameMap.class);
    assertNotSame(cloned, map);
    assertEquals(s.serialize(map), s.serialize(cloned));
  }
}
