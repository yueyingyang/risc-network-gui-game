package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;

import java.beans.ConstructorProperties;

/**
 * An class represents a cloak entry
 */
public class CloakEntry extends BasicEntry {

    /**
     * Create a CloakEntry
     *
     * @param toName     is the name of the to-territory
     * @param playerName is the name of the player
     */
    @ConstructorProperties({"toName", "playerName"})
    public CloakEntry(String toName, String playerName) {
        super(null, toName, -1, playerName, null, null);
    }

    /**
     * Hide the territory for 3 turns
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        // TODO
        // effect immediately or next turn
        myInfo.consumeTech(20);
    }
}
