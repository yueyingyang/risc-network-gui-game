package edu.duke.ece651.risc.shared;

import java.util.function.Function;
import java.util.*;

public class MapView{
  GameMap toView;
  public MapView(GameMap toView){
    this.toView=toView;
  }

  public String display(){
      
    StringBuilder msg=new StringBuilder();
    /* 
    for(String name:toView.getAllPlayerNames()){
        msg.append(name+" player:\n");
        msg.append("-------------\n");
        Iterable<Territory> territories=toView.getPlayerTerritories(name);
        for(Territory territory:territories){
            msg.append(territory.getNumSoldiersInArmy()+" units in "+territory.getName());
            msg.append("(next to: ");
            boolean hasNeighbor=false;
            for(Territory neighbor: territory.getNeighbours()){
                msg.append(neighbor.getName());
                msg.append(", ");
                hasNeighbor=true;
            }
            if(hasNeighbor){
                msg.delete(msg.length()-2,msg.length());
            }
            msg.append(")\n");
        }
        msg.append("\n");
    }
    */
    return msg.toString();
    
  }
}

