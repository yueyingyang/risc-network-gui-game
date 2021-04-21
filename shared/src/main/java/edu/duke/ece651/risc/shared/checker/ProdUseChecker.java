package edu.duke.ece651.risc.shared.checker;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.entry.ActionEntry;

public class ProdUseChecker extends Checker{

    public ProdUseChecker(Checker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(ActionEntry action, GameMap map, PlayerInfo myInfo) {
        if(map.getTerritory(action.getToName())==null){
            throw new IllegalArgumentException("The target territory name is invalid!");
        }
    }
}
