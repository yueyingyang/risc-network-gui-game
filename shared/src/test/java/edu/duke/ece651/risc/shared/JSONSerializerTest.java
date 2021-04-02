package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.entry.AttackEntry;
import edu.duke.ece651.risc.shared.entry.MoveEntry;
import edu.duke.ece651.risc.shared.entry.PlaceEntry;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSerializerTest {
  @Test
  void test_serialize_and_de_map() {
    // prep a map and place units
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    List<ActionEntry> placement = new ArrayList<>();
    placement.add(new PlaceEntry("0", 1, "player1"));
    placement.add(new PlaceEntry("1", 1, "player1"));
    placement.add(new PlaceEntry("2", 1, "player1"));
    placement.add(new PlaceEntry("3", 1, "player1"));
    for (ActionEntry pe : placement) {
      pe.apply(map, null);
    }

    Serializer s = new JSONSerializer();
    String result = s.serialize(map);

    GameMap deMap = (GameMap) s.deserialize(result, GameMap.class);

    assertDoesNotThrow(() -> new MapView(deMap).display());
  }

  @Test
  void test_serialize_and_de_action() throws JsonProcessingException {
    ActionEntry placeEntry = new PlaceEntry("0", 2, "player1");
    ActionEntry place1Entry = new PlaceEntry("1", 2, "player1");
    ActionEntry attackEntry = new AttackEntry("0", "3", 1, "player1");
    ActionEntry moveEntry = new MoveEntry("0", "1", 1, "player1");
    // test serializer
    JSONSerializer s = new JSONSerializer();
    ActionEntry dePlace = (ActionEntry) s.deserialize(s.serialize(placeEntry), ActionEntry.class);
    ActionEntry deAttack = (ActionEntry) s.deserialize(s.serialize(attackEntry), ActionEntry.class);
    ActionEntry deMove = (ActionEntry) s.deserialize(s.serialize(moveEntry), ActionEntry.class);
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    assertThat(dePlace, instanceOf(PlaceEntry.class));
    assertThat(deAttack, instanceOf(AttackEntry.class));
    assertThat(deMove, instanceOf(MoveEntry.class));

    dePlace.apply(map, null);
    place1Entry.apply(map, null);
    deAttack.apply(map, null);
    deMove.apply(map, null);
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
    JSONSerializer s = new JSONSerializer();
    String listJSON = s.getOm().writerFor(new TypeReference<List<ActionEntry>>() {
    }).writeValueAsString(p);
    V1MapFactory v1f = new V1MapFactory();
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 2);
    Collection<ActionEntry> pd = s.getOm().readValue(listJSON, new TypeReference<Collection<ActionEntry>>() {
    });
    for (ActionEntry a : pd) {
      a.apply(map, null);
    }
    assertDoesNotThrow(() -> new MapView(map).display());
  }

  @Test
  void test_exception() {
    PrintStream originalStream = System.err;
    PrintStream dummyStream = new PrintStream(new OutputStream(){
      public void write(int b) {
        // NO-OP
      }
    });
    System.setErr(dummyStream);
    try {
      JSONSerializer s = new JSONSerializer();
      s.deserialize("it's not a json string", String.class);
    }finally {
      System.setErr(originalStream);
    }
  }

}
