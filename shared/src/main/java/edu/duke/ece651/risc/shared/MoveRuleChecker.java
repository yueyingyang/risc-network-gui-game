package edu.duke.ece651.risc.shared;

import java.util.*;

public class MoveRuleChecker extends Checker{
    public MoveRuleChecker(Checker next){
        super(next);
    }
    
    public void checkMyRule(ActionEntry action, GameMap map){
        Territory start=map.getTerritory(action.getFromName());
        Territory end=map.getTerritory(action.getToName());
        if(start==end){
            throw new IllegalArgumentException("The destination of move action is same as the starting point!");
        }
        if(isConnected(start,end)){
            throw new IllegalArgumentException("The destination of move action is unreachable!");
        }
        if(!start.getOwnerName().equals(end.getOwnerName())){
            throw new IllegalArgumentException("The destination of move action should be owned by the same player!");
        }
    }
    /**
     * 
     * @param start
     * @param end
     * @return
     */
    private boolean isConnected(Territory start, Territory end){
        Set<Territory> visited=new HashSet<>();
        String ownerName=start.getOwnerName();
        visited.add(start);
        return dfs(start,end,visited,ownerName);
    }
    /**
     * input the current territory and destination territory with a set
     * @param curr
     * @param end
     * @param visited
     * @param ownerName
     * @return
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
}