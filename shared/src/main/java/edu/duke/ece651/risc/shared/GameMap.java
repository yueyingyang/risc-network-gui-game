package edu.duke.ece651.risc.shared;

//import java.util.ArrayList;

import java.util.*;


public class GameMap {
    private Map<String, Territory> territoryFinder;

    /**
     * Added for jackson deserialization
     */
    public GameMap() {
    }

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

    /**
     *
     * @return all territories in the map
     */
    public ArrayList<Territory> getAllTerritories(){
        return new ArrayList<>(territoryFinder.values());
    }


    /**
     * check if two territory has a path between them within the same player's territory
     *
     * @param start from territory
     * @param end   to territory
     * @return      turn if there is a such path otherwise return false
     */
    public boolean isConnected(Territory start, Territory end){
        Set<Territory> visited=new HashSet<>();
        String ownerName=start.getOwnerName();
        visited.add(start);
        return dfs(start,end,visited,ownerName);
    }
    /**
     * input the current territory and destination territory with a set
     *
     * @param curr      the current territory in search
     * @param end       the destination territory
     * @param visited   the set record all visited territory
     * @param ownerName the name of the from and two territories' owner
     * @return          true if there is a path from curr to end otherwise return false
     */
    private boolean dfs(Territory curr, Territory end,Set<Territory> visited, String ownerName){
        for(Territory t:curr.getNeighbours()){
            if(!t.getOwnerName().equals(ownerName) || visited.contains(t)){
                continue;
            }
            if(t==end){
                return true;
            }
            visited.add(t);
            if(dfs(t,end,visited,ownerName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the given object is equal to this
     * @param o is the given object
     * @return true if the object is equal to this map
     */
    @Override
    public boolean equals(Object o) {
      if (o.getClass().equals(getClass())) {
        GameMap m = (GameMap) o;
        return territoryFinder.equals(m.territoryFinder);
      }
      return false;
    }
}
