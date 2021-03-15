package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    // test serializer
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    Serializer s = new JSONSerializer();
    String result = s.serialize(map);

    GameMap deMap = (GameMap) s.deserialize(result, GameMap.class);
    MapView mv = new MapView(deMap);

    assertEquals("player1 player:\n" +
            "-------------\n" +
            "1 units in 0 (next to: 1, 2, 3)\n" +
            "1 units in 1 (next to: 0, 2, 3)\n" +
            "\n" +
            "player2 player:\n" +
            "-------------\n" +
            "1 units in 2 (next to: 0, 1, 3)\n" +
            "1 units in 3 (next to: 0, 1, 2)\n" +
            "\n", mv.display());
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
    GameMap map = v1f.createMap(Arrays.asList("player1", "player2"), 1);
    dePlace.apply(map, null);
    place1Entry.apply(map, null);
    deAttack.apply(map, null);
    deMove.apply(map, null);
    assertDoesNotThrow(() -> new MapView(map).display());
  }
}