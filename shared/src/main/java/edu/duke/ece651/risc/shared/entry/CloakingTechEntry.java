package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;

/**
 * An class represents an entry to research the cloaking
 */
public class CloakingTechEntry extends BasicEntry {

    /**
     * Construct a CloakingTechEntry
     *
     * @param playerName is the player name
     */
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
        myInfo.researchCloakingTech();
    }
}
