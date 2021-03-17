package edu.duke.ece651.risc.shared;

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
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers"})
    public AttackEntry(String fromName, String toName, int numSoldiers) {
        super(fromName, toName, numSoldiers);
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

    /**
     * get the starting territory name
     * 
     * @return fromName is the name of starting territory
     */
    public String getFromName(){
        return fromName;
    }

    /**
     * get the destionation territory name
     * 
     * @return toName is the name of destionation territory
     */
    public String getToName(){
        return toName;
    }

    /**
     * get number of soliders
     * 
     * @return numSoldiers is the number of soliders
     */
    public int getNumSoliders(){
        return numSoldiers;
    }
}
