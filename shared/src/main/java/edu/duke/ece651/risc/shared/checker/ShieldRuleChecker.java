package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class ShieldRuleChecker extends Checker{

    public ShieldRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(myInfo.getProdCount(Constant.shield)<=0){
            throw new IllegalArgumentException("Your shield is not enough!");
        }

        Territory terr=map.getTerritory(action.getToName());
        if(!terr.getOwnerName().equals(myInfo.getName())){
            throw new IllegalArgumentException("The shield can only be added to your own territory!");
        }
    }
}
