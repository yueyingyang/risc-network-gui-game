package edu.duke.ece651.risc.shared;
import java.util.*;

public class V1MapFactory implements AbstractMapFactory{
  public MapFactory(){
  }
  
  private List<List<String>> createConnection(List<Territory> territory_list,int player_num){
      List<List<String>> connections=new ArrayList<>();
      int size=territory_list.size();
      for(int dist=1;dist<=player_num;dist++){
        for(int i=0;i<territory_list.size()-1;i++){
          List<String> connection=new ArrayList<>();
          connection.add(territory_list.get(i).getName());
          connection.add(territory_list.get((i+dist)%size).getName());
          connections.add(connection);
        }
      }
      return connections;
  }

  private List<Territory> createTerrority(List<String> nameList, int num,Map<String,Territory> territoryFinder){
        int territory_id=0;
        List<Territory> territory_list=new ArrayList<>();
        for(String playerName:nameList){
            for(int i=0;i<num;i++){
                String territory_name=""+territory_id;
                Territory temp=new Territory(territory_name);
                temp.setOwnerName(playerName);
                territoryFinder.put(territory_name,temp);
                territory_list.add(temp);
                territory_id++;
            }
        }
        return territory_list;
    }

  public GameMap createMap(List<String> nameList, int territoriesPerPlayer){
    Map<String,Territory> territoryFinder=new HashMap<>();
    List<Territory> territories=createTerrority(nameList, territoriesPerPlayer,territoryFinder);
    List<List<String>> connections=createConnection(territories,nameList.size());
    GameMap gamemap=new GameMap(connections,territoryFinder);
    return gamemap;
  }

}