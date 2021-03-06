package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class V1MapFactoryTest {
  @Test
  public void test_createMap() {
    V1MapFactory factory=new V1MapFactory();
    List<String> nameList=new ArrayList<>();
    nameList.add("Red");
    nameList.add("Blue");
    nameList.add("Green");
    GameMap map=factory.createMap(nameList,3);
    //map.setOwnerName(nameList);

    assertTrue(map.getTerritory("0").isAdjacent(map.getTerritory("1")));
    assertTrue(map.getTerritory("0").isAdjacent(map.getTerritory("8")));
    assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("2")));
    assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("4")));
    assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("5")));
  }

}
