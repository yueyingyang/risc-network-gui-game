package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;

import java.beans.ConstructorProperties;

/**
 * An entry used to upgrade soldiers
 */
public class SoldierEntry extends BasicEntry {

    /**
     * Construct a SoldierEntry object
     *
     * @param toName      is the name of the territory
     * @param fromType    is the current type of the soldier
     * @param toType      is the type of the soldier after upgrading
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     */
    @ConstructorProperties({"toName", "fromType", "toType", "numSoldiers", "playerName"})
    public SoldierEntry(String toName, String fromType, String toType,
                        int numSoldiers, String playerName) {
        super(null, toName, numSoldiers, playerName, fromType, toType);
    }

    /**
     * Apply the action on the action entry
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Territory toTerr = myMap.getTerritory(toName);
        toTerr.upgradeArmy(fromType, toType, numSoldiers, myInfo);
    }
}