package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class SpyRuleChecker extends Checker{

    public SpyRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory toTerr=map.getTerritory(action.getToName());
        if(action.getNumSoldiers() > toTerr.getNumSoldiersInArmy("0")){
            throw new IllegalArgumentException("The type 0 soldier in this territory is not enough!");
        }

        if(!toTerr.getOwnerName().equals(myInfo.getName())){
            throw new IllegalArgumentException("The from territory must belong to you!");
        }
        if(action.getNumSoldiers()*Constant.UPGRADE_TO_SPY_COST > myInfo.getTechResource()){
            throw new IllegalArgumentException("Your tech resource is not enough!");
        }
    }
}
