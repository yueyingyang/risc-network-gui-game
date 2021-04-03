package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.PlayerInfo;
import edu.duke.ece651.risc.shared.Territory;

import java.beans.ConstructorProperties;

public class FancyMoveEntry extends BasicEntry{

    /**
     * Create a FancyMoveEntry object
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param fromType    is the type of the soldier to move
     * @param playerName  is the name of the player
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers", "fromType", "playerName"})
    public FancyMoveEntry(String fromName, String toName, int numSoldiers,
                          String fromType, String playerName) {
        super(fromName, toName, numSoldiers, playerName, fromType, null);
    }

    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers, fromType);
        toTerr.addSoldiersToArmy(numSoldiers, fromType);
        int cost = myMap.computeCost(fromTerr, toTerr, numSoldiers);
        myInfo.consumeFood(cost);
    }
}
