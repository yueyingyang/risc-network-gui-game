package edu.duke.ece651.risc.shared;


public class AttackRuleChecker extends Checker{
    public AttackRuleChecker(Checker next){
        super(next);
    }

    public void checkMyRule(ActionEntry action, GameMap map){
        Territory start=map.getTerritory(action.getFromName());
        Territory end=map.getTerritory(action.getToName());
        if(!start.isAdjacent(end)){
            throw new IllegalArgumentException("expected attacked territory is adjacent to this territory!");
        }
        if(start.getOwnerName().equals(end.getOwnerName())){
            throw new IllegalArgumentException("The attacked territory should be owned by other players!");
        }
    }
}