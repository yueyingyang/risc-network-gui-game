package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;
/**
 * A class represents a place entry
 */
public class PlaceEntry implements ActionEntry {
    private String terrName;
    private int numSoldiers;

    /**
     * Construct a placeEntry
     *
     * @param terrName    is the name of the territory
     * @param numSoldiers is the number of soldiers to place on the territory
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"terrName", "numSoldiers"})
    public PlaceEntry(String terrName, int numSoldiers) {
        this.terrName = terrName;
        this.numSoldiers = numSoldiers;
    }

    /**
     * Place the soldier on the territory at the beginning
     *
     * @param myMap     is the map of the game
     * @param myChecker is the rule checker for the action
     */
    @Override
    public void apply(GameMap myMap, Checker myChecker) {
        Territory terr = myMap.getTerritory(terrName);
        Army myArmy = new BasicArmy(terr.getOwnerName(), numSoldiers);
        terr.setMyArmy(myArmy);
    }
}
