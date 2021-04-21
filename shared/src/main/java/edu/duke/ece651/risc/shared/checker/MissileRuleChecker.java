package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
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
        Territory toTerr=map.getTerritory(action.getToName());
        if(myInfo.getName().equals(toTerr.getOwnerName())){
            throw new IllegalArgumentException("The missile should be placed to an enemy territory!");
        }

        if(myInfo.getProdCount(Constant.missile)<=0){
            throw new IllegalArgumentException("Your missile is not enough!");
        }
    }
}
