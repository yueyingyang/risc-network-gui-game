package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Random;

/**
 * A class represents a basic soldier
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicSoldier implements Soldier {

    private String type;

    /**
     * Construct a Basic Soldier Object
     */
    public BasicSoldier(String type) {
        this.type = type;
    }

    /**
     * Construct a Basic Soldier Object
     */
    public BasicSoldier() {
        this("0");
    }

    /**
     * Fight with the attacker
     *
     * @param attacker is the solder that attacks the territory
     * @param myRandom is the random object set by the game
     * @return non-negative number if the soldier that defend the territory wins
     * else return negative number
     */
    @Override
    public int fight(Soldier attacker, Random myRandom) {
        int length = 20;
        int defenderRoll = myRandom.nextInt(length);
        int attackerRoll = myRandom.nextInt(length);
        return defenderRoll - attackerRoll;
    }

    /**
     * Upgrade a soldier to the indicated type
     *
     * @param toType is the indicated type
     */
    @Override
    public void upgrade(String toType) {
        type = toType;
    }

    /**
     * Check whether a soldier has the indicated type
     *
     * @param myType is the indicated type
     * @return true if the soldier is has the indicated type else false
     */
    @Override
    public boolean hasType(String myType) {
        return type.equals(myType);
    }

    /**
     * Check if the given object is equal to this
     *
     * @param myObject is the given object
     * @return true if the object is equal to this, i.e. is a BasicSoldier
     * and have the same type, else false
     */

    @Override
    public boolean equals(Object myObject) {
        if (myObject.getClass().equals(getClass())) {
            BasicSoldier s = (BasicSoldier) myObject;
            return s.hasType(type);
        }
        return false;
    }

    /**
     * get the hash code of the soldier
     *
     * @return the hash code of the soldier
     */
    @Override
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * Get the string representation of a soldier
     *
     * @return the string representation of a soldier
     */
    @Override
    public String toString() {
        return type;
    }

}
