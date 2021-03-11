package edu.duke.ece651.risc.shared;

//import java.util.ArrayList;
import java.util.*;


public class GameMap{
    private Map<String, Territory> territoryFinder;
    
    public GameMap(List<List<String>> connections,Map<String,Territory> territoryFinder){
        this.territoryFinder=territoryFinder;
        setConnection(connections);
    }

    /** 
    * help the game map to create connections based on generated connection list
    @param connections a list of connection
     */
    private void setConnection(List<List<String>> connections){
        for(List<String> connection:connections){
            Territory start=territoryFinder.get(connection.get(0));
            Territory end=territoryFinder.get(connection.get(1));

            start.addNeighbour(end);
            end.addNeighbour(start);
        }
    }

    /**
    * find a terrority with its name
    @param name the name of territory
    @return the territory with the name
     */
    public Territory getTerritory(String name){
        return territoryFinder.get(name);
    }
}