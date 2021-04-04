package edu.duke.ece651.risc.shared.entry;


import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;

import java.beans.ConstructorProperties;

public class TechEntry extends BasicEntry {

    /**
     * Construct a TechEntry to upgrade technology level of player
     *
     * @param playerName the name of the player
     */
    @ConstructorProperties("playerName")
    public TechEntry(String playerName) {
        super(null, null, -1, playerName, null, null);
    }

    /**
     * Apply the action on the action entry
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        myInfo.takeTech();
    }
}
