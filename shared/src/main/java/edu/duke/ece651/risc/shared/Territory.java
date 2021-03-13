package edu.duke.ece651.risc.shared;

import java.util.*;

/**
 * An class represents a territory
 */
public class Territory {
    private String name;
    private String ownerName;
    private Army myArmy;
    private Set<Territory> neighbours;
    private Map<String, Army> attackerBuffer;

    /**
     * Create a territory object
     *
     * @param name is the name of the territory
     */
    public Territory(String name) {
        this.name = name;
        this.myArmy = null;
        this.neighbours = new HashSet<>();
        this.attackerBuffer = new HashMap<>();
    }

    /**
     * Get the name of the territory
     *
     * @return the name of the territory
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the owner
     *
     * @return the name of the owner
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get neighbors
     *
     * @return the neighbor territories
     */
    public Set<Territory> getNeighbours() {
        return neighbours;
    }

    /**
     * Set the owner of the territory
     *
     * @param ownerName is the name of the owner
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Place the army on the territory
     *
     * @param myArmy is the army been placed on the territory
     */
    public void setMyArmy(Army myArmy) {
        this.myArmy = myArmy;
    }

    /**
     * Check if the given object is equal to this
     *
     * @param myObject is the given object
     * @return true if the object is equal to this, i.e. is an territory
     * and have the same name, else false
     */
    @Override
    public boolean equals(Object myObject) {
        if (myObject.getClass().equals(getClass())) {
            Territory terr = (Territory) myObject;
            return terr.getName().equals(getName());
        }
        return false;
    }

    /**
     * get the hash code of the territory
     *
     * @return the hash code of the territory
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Connect the territory to its neighbour
     *
     * @param neigh is an territory adjacent to the territory
     */
    public void addNeighbour(Territory neigh) {
        neighbours.add(neigh);
    }

    /**
     * Check the if the given territory is adjacent to this
     *
     * @param terr is the given territory
     * @return ture if they are adjacent else false
     */
    public boolean isAdjacent(Territory terr) {
        return neighbours.contains(terr);
    }

    /**
     * Add the given number of soldiers to myArmy
     *
     * @param numSoldiers is the number of soldiers to add
     */
    public void addSoldiersToArmy(int numSoldiers) {
        myArmy.addSoldiers(numSoldiers);
    }

    /**
     * Remove the given number of soldiers to myArmy
     *
     * @param numSoldiers is the number of soldiers to add
     */
    public void removeSoldiersFromArmy(int numSoldiers) {
        myArmy.removeSoldiers(numSoldiers);
    }

    /**
     * Get the number of soldier in myArmy
     *
     * @return the number of soldier in myArmy
     */
    public int getNumSoldiersInArmy() {
        return myArmy.getNumSoldiers();
    }

    /**
     * Add attackers to attacker buffer
     *
     * @param attacker is the army that attack the territory
     */
    public void bufferAttacker(Army attacker) {
        String owner = attacker.getOwnerName();
        if (attackerBuffer.containsKey(owner)) {
            Army curr = attackerBuffer.get(owner);
            curr.mergeForce(attacker);
        } else {
            attackerBuffer.put(owner, attacker);
        }
    }

    /**
     * Resolve combat on the territory
     *
     * @param myRandom is the random object set by the game
     */
    public void resolveCombat(Random myRandom) {
        Army defender = myArmy;
        for (Army attacker : attackerBuffer.values()) {
            defender = defender.fight(attacker, myRandom);
        }
        myArmy = defender;
        ownerName = myArmy.getOwnerName();
    }

    public int getNumSoldiersInAttacker(String name) {
        return attackerBuffer.get(name).getNumSoldiers();
    }
}
