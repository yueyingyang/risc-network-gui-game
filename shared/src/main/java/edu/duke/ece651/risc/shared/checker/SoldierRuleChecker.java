package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.GameUtil;

public class SoldierRuleChecker extends Checker{
    public SoldierRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(myInfo.getTechResource()< GameUtil.getSoldierCost(action.getFromType(),action.getToType(),action.getNumSoldiers())){
            throw new IllegalArgumentException("Your Technology resource is not enough!");
        }
        if(action.getToType().compareTo(action.getFromType())<0){
            throw new IllegalArgumentException("You can only upgrade solider instead of downgrading!");
        }
        if(GameUtil.getTechRequire(action.getToType())>myInfo.getTechLevel()){
            throw new IllegalArgumentException("Soldier's level can't be higher than the player's level!");
        }
    }
}
