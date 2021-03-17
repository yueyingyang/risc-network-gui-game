package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Collection;

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
    placement.add(new PlaceEntry("0", 1));
    placement.add(new PlaceEntry("1", 1));
    placement.add(new PlaceEntry("2", 1));
    placement.add(new PlaceEntry("3", 1));
    for (ActionEntry pe : placement) {
      pe.apply(map, null);
    }

    Serializer s = new JSONSerializer();
    String result = s.serialize(map);

    GameMap deMap = (GameMap) s.deserialize(result, GameMap.class);

    assertDoesNotThrow(() -> new MapView(map).display());
  }

  @Test
  void test_serialize_and_de_action() throws JsonProcessingException {
    ActionEntry placeEntry = new PlaceEntry("0", 2);
    ActionEntry place1Entry = new PlaceEntry("1", 2);
    ActionEntry attackEntry = new AttackEntry("0", "1", 1);
    ActionEntry moveEntry = new MoveEntry("0", "1", 1);
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
    p.add(new PlaceEntry("0", 2));
    p.add(new PlaceEntry("1", 2));
    p.add(new AttackEntry("0", "1", 1));
    p.add(new MoveEntry("0", "1", 1));
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

  @Disabled
  @Test
  void test_serialize_string() {
    JSONSerializer s = new JSONSerializer();
    assertEquals(Constant.NO_GAME_AVAILABLE_INFO, s.serialize(Constant.NO_GAME_AVAILABLE_INFO));
  }
}