package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class CloakingTechRuleChecker extends Checker{
    public CloakingTechRuleChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(myInfo.getTechLevel() < 3){
            throw new IllegalArgumentException("Your tech level is not enough!");
        }
        if(myInfo.getTechResource() < Constant.RESEARCH_CLOAKING_COST){
            throw new IllegalArgumentException("Your tech resource is not enough!");
        }
        if (myInfo.hasCloakingTech()) {
            throw new IllegalArgumentException("Your have researched Clocking before!");
        }
    }
}
