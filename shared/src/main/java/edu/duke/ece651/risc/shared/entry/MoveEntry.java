package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.entry.BasicEntry;

import java.beans.ConstructorProperties;

public class MoveEntry extends BasicEntry {

    /**
     * Construct a move entry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers", "playerName"})
    public MoveEntry(String fromName, String toName, int numSoldiers, String playerName) {
        super(fromName, toName, numSoldiers, playerName);
    }

    /**
     * Move soldiers from one territory to another
     *
     * @param myMap is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker myChecker = new ClientChecker(new MoveRuleChecker(null));
        myChecker.checkAction(this, myMap);
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        toTerr.addSoldiersToArmy(numSoldiers);
    }
}
