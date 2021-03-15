package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;

public class MoveEntry implements ActionEntry {
    private String fromName;
    private String toName;
    private int numSoldiers;

    /**
     * Construct a move entry
     *
     * @param fromName    is the name of the territory where the soldiers are from
     * @param toName      is the name of the territory where the soldiers go to
     * @param numSoldiers is the number of soldiers to move
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"fromName", "toName", "numSoldiers"})
    public MoveEntry(String fromName, String toName, int numSoldiers) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
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
}
