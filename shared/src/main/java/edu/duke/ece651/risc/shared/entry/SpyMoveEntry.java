package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.SpyMoveRuleChecker;
import edu.duke.ece651.risc.shared.checker.SpyRuleChecker;

import java.beans.ConstructorProperties;

/**
 * An entry to move the spy
 */
public class SpyMoveEntry extends BasicEntry {
    /**
     * Construct a SpyMoveEntry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the player name
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers", "playerName"})
    public SpyMoveEntry(String fromName, String toName, int numSoldiers,
                        String playerName) {
        super(fromName, toName, numSoldiers, playerName, "0", null);
    }

    /**
     * Apply the entry
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker checker = new SpyMoveRuleChecker(null);
        checker.checkAction(this,myMap,myInfo);
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSpies(playerName, numSoldiers);
        toTerr.bufferSpies(playerName, numSoldiers);
    }
}
