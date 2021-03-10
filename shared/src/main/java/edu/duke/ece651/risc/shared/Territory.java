package edu.duke.ece651.risc.shared;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An class represents a territory
 */
public class Territory {
    private String name;
    private Army defender;
    private Set<Territory> neighbours;
    private Map<String, Army> attackerBuffer;

    /**
     * Create a territory object
     *
     * @param name is the name of the territory
     */
    public Territory(String name) {
        this.name = name;
        this.defender = null;
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
     * Set the defender on the territory
     *
     * @param defender is an army that defends the territory
     */
    public void setDefender(Army defender) {
        this.defender = defender;
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
            Territory territory = (Territory) myObject;
            return territory.getName().equals(getName());
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
     * @param neighbour is an territory adjacent to the territory
     */
    public void addNeighbour(Territory neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * Check the if the given territory is adjacent to this
     *
     * @param territory is the given territory
     * @return ture if they are adjacent else false
     */
    public boolean isAdjacent(Territory territory) {
        return neighbours.contains(territory);
    }

}
