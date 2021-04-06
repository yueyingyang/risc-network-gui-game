package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.V1MapFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class V2MapFactoryTest {
    @Test
    public void test_createMap() {
        V2MapFactory factory=new V2MapFactory();
        List<String> nameList=new ArrayList<>();
        nameList.add("Red");
        nameList.add("Blue");
        nameList.add("Green");
        GameMap map=factory.createMap(nameList,3);
        //map.setOwnerName(nameList);

        assertTrue(map.getTerritory("0").isAdjacent(map.getTerritory("1")));
        assertTrue(map.getTerritory("0").isAdjacent(map.getTerritory("8")));
        assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("2")));
        assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("3")));
        assertFalse(map.getTerritory("0").isAdjacent(map.getTerritory("6")));
        assertTrue(map.getTerritory("0").isAdjacent(map.getTerritory("4")));
    }

}