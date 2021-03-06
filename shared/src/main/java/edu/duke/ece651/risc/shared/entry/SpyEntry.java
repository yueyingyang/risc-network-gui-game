package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.SpyRuleChecker;

import java.beans.ConstructorProperties;

/**
 * An entry used to upgrade type-0 soldiers to spies
 */
public class SpyEntry extends BasicEntry {

    /**
     * Construct a SpyEntry object
     *
     * @param toName      is the name of the territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the player name
     */
    @ConstructorProperties({"toName", "numSoldiers", "playerName"})
    public SpyEntry(String toName, int numSoldiers, String playerName) {
        super(null, toName, numSoldiers, playerName, null, null);
    }

    /**
     * Apply the action on the action entry
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker checker = new SpyRuleChecker(null);
        checker.checkAction(this,myMap,myInfo);
        Territory terr = myMap.getTerritory(toName);
        terr.removeSoldiersFromArmy(numSoldiers);
        terr.addSpies(playerName, numSoldiers);
        int cost = numSoldiers * Constant.UPGRADE_TO_SPY_COST;
        myInfo.consumeTech(cost);
    }
}
