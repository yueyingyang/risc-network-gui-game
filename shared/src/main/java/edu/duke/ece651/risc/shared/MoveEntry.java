package edu.duke.ece651.risc.shared;

public class MoveEntry implements ActionEntry{
    private String fromName;
    private String toName;
    private int numSoldiers;

    /**
     * Move soldiers from one territory to another
     *
     * @param myMap is the map of the game
     */
    @Override
    public void apply(GameMap myMap) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        toTerr.addSoldiersToArmy(numSoldiers);
    }
}
