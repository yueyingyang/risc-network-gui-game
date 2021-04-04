package edu.duke.ece651.risc.shared;
import java.util.*;

public class V2MapFactory implements AbstractMapFactory{
    public V2MapFactory(){
    }


    /**
     * create a connection list make sure the generated map are connected
     * it connects all territory within a certain distance
     * for example if the input distance is 2, connect the territory 0 with 1,2, N-1, N-2
     * @param territory_list the list of all territories in the map
     * @return a list of connections
     */
    private List<List<String>> createConnection(List<Territory> territory_list){
        List<List<String>> connections=new ArrayList<>();
        int size=territory_list.size();
        for(int dist=1;dist<=2;dist++){
            for(int i=0;i<territory_list.size();i++){
                List<String> connection=new ArrayList<>();
                connection.add(territory_list.get(i).getName());
                connection.add(territory_list.get((i+dist)%size).getName());
                connections.add(connection);
            }
        }
        return connections;
    }

    /**
     * create territories based on players' name list and the num of territories per player.
     * After creation put all created territories in a list, and a name-territory hashmap
     * @param nameList players' name list
     * @param num the num of territories per player
     * @param territoryFinder the hashmap for the relationship of name-sterritory, we will add key and value in this method
     * @return a list of territories
     */
    private List<Territory> createTerrority(List<String> nameList, int num,Map<String,Territory> territoryFinder){
        int territory_id=0;
        List<Territory> territory_list=new ArrayList<>();
        for(String playerName: nameList){
            for(int i=0;i<num;i++){
                String territory_name=""+territory_id;
                Territory temp=new Territory(territory_name,5,20,20);
                temp.setOwnerName(playerName);
                territoryFinder.put(territory_name,temp);
                territory_list.add(temp);
                territory_id++;
            }
        }
        return territory_list;
    }

    /**
     *create a game map object for version1's game
     @param nameList player's name list
     @param territoriesPerPlayer how many territories each player have
     @return a game map
     */
    public GameMap createMap(List<String> nameList, int territoriesPerPlayer){
        Map<String,Territory> territoryFinder=new HashMap<>();
        List<Territory> territories=createTerrority(nameList, territoriesPerPlayer,territoryFinder);
        List<List<String>> connections=createConnection(territories);
        return new GameMap(connections,territoryFinder);
    }

}