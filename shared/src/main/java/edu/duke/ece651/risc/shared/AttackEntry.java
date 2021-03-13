package edu.duke.ece651.risc.shared;

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
     */
    public AttackEntry(String fromName, String toName, int numSoldiers) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
    }

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
