package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;
import edu.duke.ece651.risc.shared.game.GameUtil;

public class TechRuleChecker extends Checker{
    public TechRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(myInfo.getTechLevel()>=6){
            throw new IllegalArgumentException("Your Technology level is ready the highest!");
        }
        if(myInfo.getTechResource()<GameUtil.getTechCost(myInfo.getTechLevel())){
            throw new IllegalArgumentException("Your Technology resource is not enough!");
        }
        if(myInfo.isRequested()){
            throw new IllegalArgumentException("Your can't upgrade twice in one turn!");
        }
    }
}
