package edu.duke.ece651.risc.shared;

import java.util.function.Function;

public class MapView{
  GameMap toView;
  public MapView(GameMap toView){
    this.toView=toView;
  }

  public String display(){
    StringBuilder msg="";

    for(String name:toView.getAllPlayerNames){
        msg.append(name+" player:\n");
        msg.append("-------------\n");
        Iterable<Territory> territories=getPlayerTerritories(name);
        for(Territory territory:territories){
            msg.append(territory.getNumSoldiersInArmy()+" units in "+territory.getName());
            msg.append("(next to: ");
            for(Territory neighbor: territory.getNeighbors()){
                msg.append(neighbor.getName());
                msg.append(", ")
            }
            msg.delete(msg.length()-2,msg.length());
            msg.append("\n");
        }
        msg.append("\n")
    }

    return msg.toString();
  }
}

