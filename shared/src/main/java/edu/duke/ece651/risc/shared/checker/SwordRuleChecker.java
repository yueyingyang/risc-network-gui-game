package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class SwordRuleChecker extends Checker{

    public SwordRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        // check if the player has enough sword
        if(myInfo.getProdCount(Constant.sword)<=0){
            throw new IllegalArgumentException("Your sword is not enough!");
        }

        // the sword can only be used on enemy's territory
        Territory terr=map.getTerritory(action.getToName());
        if(terr.getOwnerName().equals(myInfo.getName())){
            throw new IllegalArgumentException("The sword can only be added to enemy's territory!");
        }
    }
}
