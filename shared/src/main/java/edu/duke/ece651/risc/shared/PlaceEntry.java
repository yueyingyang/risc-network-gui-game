package edu.duke.ece651.risc.shared;

/**
 * A class represents a place entry
 */
public class PlaceEntry implements ActionEntry {
    private String terrName;
    private int numSoldiers;

    /**
     * Place the soldier on the territory at the beginning
     *
     * @param myMap is the map of the game
     */
    @Override
    public void apply(GameMap myMap) {
        Territory terr = myMap.getTerritory(terrName);
        Army myArmy = new BasicArmy(terr.getOwnerName(), numSoldiers);
        terr.setMyArmy(myArmy);
    }
}
