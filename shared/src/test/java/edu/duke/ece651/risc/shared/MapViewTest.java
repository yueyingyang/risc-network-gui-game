package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

public class MapViewTest {
    @Test
    public void test_display() {
        GameMap map = getGameMap(3, Arrays.asList("John", "Tom"));

        MapView view = new MapView(map);

        String actual = view.display();

        String expected = "Tom player:\n" +
                "-------------\n" +
                "1 units in 3 (next to: 2, 4)\n" +
                "1 units in 4 (next to: 3, 5)\n" +
                "1 units in 5 (next to: 0, 4)\n" +
                "\n" +
                "John player:\n" +
                "-------------\n" +
                "1 units in 0 (next to: 1, 5)\n" +
                "1 units in 1 (next to: 0, 2)\n" +
                "1 units in 2 (next to: 1, 3)\n" +
                "\n";
        assertEquals(expected, actual);
        String expectedEmptyMap = "Tom player:\n" +
                "-------------\n" +
                "3 (next to: 2, 4)\n" +
                "4 (next to: 3, 5)\n" +
                "5 (next to: 0, 4)\n" +
                "\n" +
                "John player:\n" +
                "-------------\n" +
                "0 (next to: 1, 5)\n" +
                "1 (next to: 0, 2)\n" +
                "2 (next to: 1, 3)\n" +
                "\n";
        assertEquals(expectedEmptyMap,view.displayMapShape());
    }

    public static GameMap getGameMap(int i, List<String> playerNames) {
        V1MapFactory f1 = new V1MapFactory();
        GameMap map = f1.createMap(playerNames, i);

        for (String playerName : map.getAllPlayerTerritories().keySet()) {
            for (Territory t : map.getPlayerTerritories(playerName)) {
                t.setMyArmy(new BasicArmy(playerName, 1));
            }
        }
        return map;
    }

    @Test
    void display_valid_neigh(){
        GameMap map = getGameMap(1, Arrays.asList("John", "Tom"));
        assertEquals("Tom player:\n" +
                "-------------\n" +
                "1 units in 1 (next to: 0)\n" +
                "\n" +
                "John player:\n" +
                "-------------\n" +
                "1 units in 0 (next to: 1)\n" +
                "\n", new MapView(map).display());
    }
}
