package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.checker.AttackRuleChecker;
import edu.duke.ece651.risc.shared.checker.Checker;
import edu.duke.ece651.risc.shared.checker.ClientChecker;

import java.beans.ConstructorProperties;

/**
 * A class represents an attack entry
 */
public class AttackEntry extends BasicEntry {

    /**
     * Create an attack entry object
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers", "playerName"})
    public AttackEntry(String fromName, String toName, int numSoldiers, String playerName) {
        super(fromName, toName, numSoldiers, playerName, null, null);
    }

    /**
     * Send attackers
     *
     * @param myMap  is the map of the game
     * @param myInfo is the player info
     */
    @Override
    public void apply(GameMap myMap, PlayerInfo myInfo) {
        Checker myChecker = new ClientChecker(new AttackRuleChecker(null));
        myChecker.checkAction(this, myMap,myInfo);
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        Army attacker = new Army(fromTerr.getOwnerName(), numSoldiers);
        toTerr.bufferAttacker(attacker);
    }
}
