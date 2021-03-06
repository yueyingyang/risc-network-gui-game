package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class FancyMoveRuleChecker extends Checker {
    public FancyMoveRuleChecker(Checker next) {
        super(next);
    }

    /**
     * check 3 rules:
     * 1. the "from territory" and "to territory" should be different
     * 2. the "from territory" and "to territory" should belong to the same owners.
     * 3. there should be a path from "from territory" to "to territory" within one player's territory
     */
    public void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory start = map.getTerritory(action.getFromName());
        Territory end = map.getTerritory(action.getToName());


        if (start.equals(end)) {
            throw new IllegalArgumentException("The destination of move action is same as the starting point!");
        }

        if (!start.belongToSameOwner(end)) {
            throw new IllegalArgumentException("The destination of move action should be owned by the same player!");
        }

        if (!map.isConnected(start, end)) {
            throw new IllegalArgumentException("The destination of move action is unreachable!");
        }

        if(map.computeCost(start,end,action.getNumSoldiers())>myInfo.getFoodResource()){
            throw new IllegalArgumentException("The food resource is not enough!");
        }
    }

}