package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.HashMap;


public class GameMap implements AbstractMapFactory{
    private Map<String, Territory> territoryFinder;
    private List<String> playerNameList;
    public GameMap(List<String> nameList, int territoriesPerPlayer){
        territoryFinder=new HashMap<>();
        this.playerNameList=nameList;
        createTerrority(nameList, territoriesPerPlayer);
    }
    
    private void createTerrority(List<String> nameList, int num){
        int territory_id=0;
        for(String playerName:nameList){
            for(int i=0;i<num;i++){
                String territory_name=""+territory_id;
                Territory temp=new Territory(territory_name);
                temp.setOwnerName(playerName);
                territoryFinder.put(territory_name,temp);
                territory_id++；
            }
        }
    }

    public Iterable<String> getAllPlayerNames(){
        return playerNameList;
    }

    public Iterable<Territory> getPlayerTerritories(String playerName){
        List<Territory> territory_list=new ArrayList<>();
        for(String name:territoryFinder.keySet()){
            Territory t=territoryFinder.get(name);
            if(playerName.equals(t.getOwnerName())){
                territory_list.add(t);
            }
        }
    }

    public Territory getTerritoryByName(String name){
        return territoryFinder.get(name);
    }
}