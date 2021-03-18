package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;

/**
 * A class represents a place entry
 */
public class PlaceEntry extends BasicEntry {

    /**
     * Construct a placeEntry
     *
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"toName", "numSoldiers"})
    public PlaceEntry(String toName, int numSoldiers) {
        super(null, toName, numSoldiers);
    }

    /**
     * Place the soldier on the territory at the beginning
     *
     * @param myMap is the map of the game
     */
    @Override
    public void apply(GameMap myMap) {
        Territory terr = myMap.getTerritory(toName);
        Army myArmy = new BasicArmy(terr.getOwnerName(), numSoldiers);
        terr.setMyArmy(myArmy);
    }
}
