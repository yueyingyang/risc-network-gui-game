package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class SpyMoveRuleChecker extends Checker{
    public SpyMoveRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory toTerr=map.getTerritory(action.getToName());
        Territory fromTerr=map.getTerritory(action.getFromName());
        if(toTerr==null || fromTerr==null){
            throw new IllegalArgumentException("The input territory is invalid!");
        }
        if(!toTerr.isAdjacent(fromTerr)){
            throw new IllegalArgumentException("Spy soldier can only be moved to adjacent territory in one turn!");
        }
        if(action.getNumSoldiers()>fromTerr.getNumSpies(myInfo.getName())){
            throw new IllegalArgumentException("Spy soldier in this territory is not enough!");
        }
    }
}
