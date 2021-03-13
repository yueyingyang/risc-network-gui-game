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
            if(start==end) continue;
            start.addNeighbour(end);
            end.addNeighbour(start);
        }
    }

    public void setOwnerName(List<String> playerNames){
        int numTerritories=territoryFinder.size();
        //assert(numTerritories%playerNames==0);
        boolean[] visited=new boolean[numTerritories];
        Random rand=new Random();
        List<Territory> territories=new ArrayList<>();
        for(String terrName:territoryFinder.keySet()){
            territories.add(territoryFinder.get(terrName));
        }
        for(String name:playerNames){
            int count=0;
            while(count<numTerritories/playerNames.size()){
                int i=rand.nextInt(numTerritories);
                if(visited[i]) continue;
                visited[i]=true;
                territories.get(i).setOwnerName(name);
                count++;
            }
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
            if (playerName.equals(territoryFinder.get(name).getOwnerName())) {
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
        ArrayList<Territory> territories = new ArrayList<Territory>();
        for(Map.Entry<String, Territory> entry : this.territoryFinder.entrySet()){
            territories.add(entry.getValue());
        }
        return territories;
    }
}