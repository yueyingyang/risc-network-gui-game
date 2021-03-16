package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

public class MapViewTest {
    @Test
    public void test_display() {
        V1MapFactory f1 = new V1MapFactory();
        List<String> playerNames = new ArrayList<>();
        playerNames.add("John");
        playerNames.add("Tom");
        GameMap map = f1.createMap(playerNames, 3);

        for (String playerName : map.getAllPlayerTerritories().keySet()) {
            for (Territory t : map.getPlayerTerritories(playerName)) {
                t.setMyArmy(new BasicArmy(playerName, 1));
            }
        }

        MapView view = new MapView(map);

        String actual = view.display();

        String expected = "Tom player:\n" +
                "-------------\n" +
                "1 units in 3 (next to: 1, 2, 4, 5)\n" +
                "1 units in 4 (next to: 0, 2, 3, 5)\n" +
                "1 units in 5 (next to: 0, 1, 3, 4)\n" +
                "\n" +
                "John player:\n" +
                "-------------\n" +
                "1 units in 0 (next to: 1, 2, 4, 5)\n" +
                "1 units in 1 (next to: 0, 2, 3, 5)\n" +
                "1 units in 2 (next to: 0, 1, 3, 4)\n" +
                "\n";
        assertEquals(expected, actual);
        String expectedEmptyMap = "Tom player:\n" +
                "-------------\n" +
                "3 (next to: 1, 2, 4, 5)\n" +
                "4 (next to: 0, 2, 3, 5)\n" +
                "5 (next to: 0, 1, 3, 4)\n" +
                "\n" +
                "John player:\n" +
                "-------------\n" +
                "0 (next to: 1, 2, 4, 5)\n" +
                "1 (next to: 0, 2, 3, 5)\n" +
                "2 (next to: 0, 1, 3, 4)\n" +
                "\n";
        assertEquals(expectedEmptyMap,view.displayMapShape());
    }

    @Test
    void display_valid_neigh(){
        V1MapFactory f1 = new V1MapFactory();
        List<String> playerNames = new ArrayList<>();
        playerNames.add("John");
        playerNames.add("Tom");
        GameMap map = f1.createMap(playerNames, 1);

        for (String playerName : map.getAllPlayerTerritories().keySet()) {
            for (Territory t : map.getPlayerTerritories(playerName)) {
                t.setMyArmy(new BasicArmy(playerName, 1));
            }
        }
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
