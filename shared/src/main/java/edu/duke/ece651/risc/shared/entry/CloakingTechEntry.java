package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.CloakingTechRuleChecker;

import java.beans.ConstructorProperties;

/**
 * An class represents an entry to research the cloaking
 */
public class CloakingTechEntry extends BasicEntry {

    /**
     * Construct a CloakingTechEntry
     *
     * @param playerName is the player name
     */
    @ConstructorProperties("playerName")
    public CloakingTechEntry(String playerName) {
        super(null, null, -1, playerName, null, null);
    }

    /**
     * Apply the entry
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker checker = new CloakingTechRuleChecker(null);
        checker.checkAction(this,myMap,myInfo);
        myInfo.researchCloakingTech();
    }
}
