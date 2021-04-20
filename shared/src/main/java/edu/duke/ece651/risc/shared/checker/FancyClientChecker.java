package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class FancyClientChecker extends Checker{
    public FancyClientChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        Territory start = map.getTerritory(action.getFromName());
        if (start.getNumSoldiersInArmy(action.getFromType()) < action.getNumSoldiers()) {
            throw new IllegalArgumentException("The army in this territory is not enough for this action!");
        }
    }
}
