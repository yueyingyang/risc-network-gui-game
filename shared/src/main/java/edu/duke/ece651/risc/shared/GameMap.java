package edu.duke.ece651.risc.shared;

//import java.util.ArrayList;
import java.util.*;


public class GameMap{
    private Map<String, Territory> territoryFinder;
    
    public GameMap(List<List<String>> connections,Map<String,Territory> territoryFinder){
        this.territoryFinder=territoryFinder;
        setConnection(connections);
    }

    public void setConnection(List<List<String>> connections){
        for(List<String> connection:connections){
            Territory start=territoryFinder.get(connection.get(0));
            Territory end=territoryFinder.get(connection.get(1));

            start.addNeighbour(end);
            end.addNeighbour(start);
        }
    }

    /*
    public Iterable<Territory> getPlayerTerritories(String playerName){
        List<Territory> territory_list=new ArrayList<>();
        for(String name:territoryFinder.keySet()){
            Territory t=territoryFinder.get(name);
            if(playerName.equals(t.getOwnerName())){
                territory_list.add(t);
            }
        }
        return territory_list;
    }
    */

    public Territory getTerritory(String name){
        return territoryFinder.get(name);
    }
}