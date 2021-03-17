package edu.duke.ece651.risc.shared;

public class ClientChecker extends Checker{
    public ClientChecker(Checker next){
        super(next);
    }

    public void checkMyRule(ActionEntry action, GameMap map){
        if(map.getTerritory(action.getFromName())==null){
            throw new IllegalArgumentException("The start territory name is invalid!");
        }
        if(map.getTerritory(action.getToName())==null){
            throw new IllegalArgumentException("The destination territory name is invalid!");
        }
        Territory start=map.getTerritory(action.getFromName());

        if(start.getNumSoldiersInArmy()<action.getNumSoldiers()){
            throw new IllegalArgumentException("The army in this territory is not enough for this action!\n");
        }
    }
}