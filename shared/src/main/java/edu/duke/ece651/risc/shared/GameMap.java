package edu.duke.ece651.risc.shared;

//import java.util.ArrayList;

import java.util.*;


public class GameMap {
    private final Map<String, Territory> territoryFinder;

    public GameMap(List<List<String>> connections, Map<String, Territory> territoryFinder) {
        this.territoryFinder = territoryFinder;
        setConnection(connections);
    }

    /**
     * help the game map to create connections based on generated connection list
     *
     * @param connections a list of connection
     */
    private void setConnection(List<List<String>> connections) {
        for (List<String> connection : connections) {
            Territory start = territoryFinder.get(connection.get(0));
            Territory end = territoryFinder.get(connection.get(1));

            start.addNeighbour(end);
            end.addNeighbour(start);
        }
    }

    /**
     * find a terrority with its name
     *
     * @param name the name of territory
     * @return the territory with the name
     */
    public Territory getTerritory(String name) {
        return territoryFinder.get(name);
    }

    /**
     * return all territory belong to a player
     *
     * @param playerName is the name of the player want to fetch all his territories
     * @return all territories belong to this player
     */
    public Iterable<Territory> getPlayerTerritories(String playerName) {
        Set<Territory> territorySet = new HashSet<>();
        for (String name : territoryFinder.keySet()) {
            if (territoryFinder.get(name).getOwnerName().equals(playerName)) {
                territorySet.add(territoryFinder.get(name));
            }
        }
        return territorySet;
    }

    /**
     * Group by territories by players
     *
     * @return a map, key is the name of the player, value is the territories belong to me
     */
    public Map<String, Set<Territory>> getAllPlayerTerritories() {
        Map<String, Set<Territory>> playerMap = new HashMap<>();
        for (String name : territoryFinder.keySet()) {
            Territory t = territoryFinder.get(name);
            if (playerMap.containsKey(t.getOwnerName())) {
                playerMap.get(t.getOwnerName()).add(t);
            } else {
                Set<Territory> territorySet = new HashSet<>();
                territorySet.add(t);
                playerMap.put(t.getOwnerName(), territorySet);
            }
        }
        return playerMap;
    }
}