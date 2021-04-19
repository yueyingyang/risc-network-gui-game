package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class MissileRuleChecker extends Checker{
    public MissileRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(map.getTerritory(action.getToName())==null){
            throw new IllegalArgumentException("Invalid territory name!");
        }
        Territory toTerr=map.getTerritory(action.getToName());
        if(myInfo.getName().equals(toTerr.getOwnerName())){
            throw new IllegalArgumentException("The missile should be placed to an enemy territory!");
        }
    }
}
