package edu.duke.ece651.risc.shared;

public class ClientChecker extends Checker{
    public ClientChecker(Checker next){
        super(next);
    }

    public void checkMyRule(ActionEntry action, GameMap map){
        if(map.getTerritory(action.getFromName())==null){
            throw new IllegalArgumentException();
        }
        if(map.getTerritory(action.getToName())==null){
            throw new IllegalArgumentException();
        }
        Territory start=map.getTerritory(action.getFromName());

        if(start.getNumSoldiersInArmy()<action.getNumSoliders()){
            throw new IllegalArgumentException("The army in this territoy is not enough for this action!\n");
        }

    }
}