package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class ClockRuleChecker extends Checker{
    public ClockRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory toTerr=map.getTerritory(action.getToName());
        if(toTerr==null){
            throw new IllegalArgumentException("Invalid territory name!");
        }
        if(!toTerr.getOwnerName().equals(myInfo.getName())){
            throw new IllegalArgumentException("The chosen territory should be your own territory!");
        }
        if(myInfo.getTechResource() < Constant.ORDER_CLOAKING_COST){
            throw new IllegalArgumentException("Your tech resource is not enough!");
        }
        if (!myInfo.hasCloakingTech()) {
            throw new IllegalArgumentException("You need to research cloaking tech first!");
        }
    }
}
