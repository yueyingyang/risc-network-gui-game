package edu.duke.ece651.risc.shared;

/**
 * A class represents an attack entry
 */
public class AttackEntry implements ActionEntry {
    private String fromName;
    private String toName;
    private int numSoldiers;

    /**
     * Send attackers
     *
     * @param myMap is the map of the game
     */
    @Override
    public void apply(GameMap myMap) {
        Territory fromTerr = myMap.getTerritory(fromName);
        Territory toTerr = myMap.getTerritory(toName);
        fromTerr.removeSoldiersFromArmy(numSoldiers);
        Army attacker = new BasicArmy(fromTerr.getOwnerName(), numSoldiers);
        toTerr.bufferAttacker(attacker);
    }
}
