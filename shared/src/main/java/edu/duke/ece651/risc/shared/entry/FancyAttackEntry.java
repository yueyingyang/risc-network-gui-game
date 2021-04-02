package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;

import java.beans.ConstructorProperties;

public class FancyAttackEntry extends BasicEntry {
    /**
     * Create a FancyAttackEntry object
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param fromType    is the type of the soldier participating in the attack
     * @param playerName  is the name of the player
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers", "fromType", "playerName"})
    public FancyAttackEntry(String fromName, String toName, int numSoldiers, String fromType, String playerName) {
        super(fromName, toName, numSoldiers, playerName, fromType, null);
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
        myChecker.checkAction(this, myMap);
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers, fromType);
        Army attacker = new BasicArmy(fromTerr.getOwnerName(), numSoldiers, fromType);
        toTerr.bufferAttacker(attacker);
        myInfo.consumeFood(numSoldiers);
    }

}
