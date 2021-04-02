package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.Army;
import edu.duke.ece651.risc.shared.BasicArmy;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.entry.BasicEntry;

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
     * @param playerName  is the name of the player
     * @annotation ConstructorProperties is for JSON deserialization
     */
    @ConstructorProperties({"toName", "numSoldiers", "playerName"})
    public PlaceEntry(String toName, int numSoldiers, String playerName) {
        super(null, toName, numSoldiers, playerName);
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
