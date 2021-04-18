package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.Constant;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;

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
        Territory terr = myMap.getTerritory(toName);
        terr.removeSoldiersFromArmy(numSoldiers);
        terr.addMySpies(numSoldiers);
        int cost = numSoldiers * Constant.UPGRADE_TO_SPY_COST;
        myInfo.consumeTech(cost);
    }
}
