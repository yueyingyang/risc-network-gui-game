package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;

/**
 * A class represents an attack entry
 */
public class AttackEntry implements ActionEntry {
    private String fromName;
    private String toName;
    private int numSoldiers;

    /**
     * Create an attack entry object
     *
     * @param fromName    is the name of the territory where the attacker is from
     * @param toName      is the name of the territory to attack
     * @param numSoldiers is the number of soldiers participating in the attack
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers"})
    public AttackEntry(String fromName, String toName, int numSoldiers) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
    }

    /**
     * Send attackers
     *
     * @param myMap     is the map of the game
     * @param myChecker is the rule checker for the action
     */
    @Override
    public void apply(GameMap myMap, Checker myChecker) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        Army attacker = new BasicArmy(fromTerr.getOwnerName(), numSoldiers);
        toTerr.bufferAttacker(attacker);
    }
}
