package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;

public class MoveEntry extends BasicEntry {

    /**
     * Construct a move entry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers"})
    public MoveEntry(String fromName, String toName, int numSoldiers) {
        super(fromName, toName, numSoldiers);
    }

    /**
     * Move soldiers from one territory to another
     *
     * @param myMap     is the map of the game
     * @param myChecker is the rule checker for the action
     */
    @Override
    public void apply(GameMap myMap, Checker myChecker) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        toTerr.addSoldiersToArmy(numSoldiers);
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
