package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.GameUtil;

public class SoldierRuleChecker extends Checker{
    public SoldierRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory start = map.getTerritory(action.getToName());

        if(start==null){
            throw new IllegalArgumentException("Invalid territory name!");
        }

        if(!start.getOwnerName().equals(action.getPlayerName())){
            throw new IllegalArgumentException("Don't modify other player's territory!");
        }

        if(action.getNumSoldiers()<0){
            throw new IllegalArgumentException("The input unit should not be negative!");
        }
        String soldierLevel=action.getFromType();
        if (start.getNumSoldiersInArmy(soldierLevel) < action.getNumSoldiers()) {
            throw new IllegalArgumentException("The army in this territory is not enough for this action!");
        }

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
