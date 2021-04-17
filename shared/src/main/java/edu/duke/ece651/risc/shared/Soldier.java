package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.duke.ece651.risc.shared.game.GameUtil;

import java.util.Random;

/**
 * A class represents a Soldier
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Soldier implements Comparable<Soldier> {

    private String type;

    /**
     * Construct a Basic Soldier Object
     */
    public Soldier(String type) {
        this.type = type;
    }

    /**
     * Construct a Soldier Object
     */
    public Soldier() {
        this("0");
    }

    /**
     * Copy constructor of Soldier
     * @param s is a soldier
     */
    public Soldier(Soldier s) {
        this.type = s.type;
    }

    /**
     * Fight with the attacker
     *
     * @param attacker is the solder that attacks the territory
     * @param myRandom is the random object set by the game
     * @return non-negative number if the soldier that defend the territory wins
     * else return negative number
     */
    public int fight(Soldier attacker, Random myRandom) {
        int length = 20;
        int defenderRoll = myRandom.nextInt(length) + GameUtil.getBonus(type);
        int attackerRoll = myRandom.nextInt(length) + GameUtil.getBonus(attacker.getType());
        return defenderRoll - attackerRoll;
    }

    /**
     * Get the type of the soldier
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Check whether a soldier has the indicated type
     *
     * @param myType is the indicated type
     * @return true if the soldier is has the indicated type else false
     */
    protected boolean hasType(String myType) {
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
            Soldier s = (Soldier) myObject;
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
     * Set the natural ordering of the two soldiers based on the bonus
     * to sort in descending order
     *
     * @param s is the soldier to compare to
     * @return positive number if the bonus is smaller
     */
    public int compareTo(Soldier s) {
        return GameUtil.getBonus(s.getType()) - GameUtil.getBonus(type);
    }

    /**
     * Get the string representation of the soldier
     *
     * @return the string representation of the soldier
     */
    @Override
    public String toString() {
        return type;
    }
}
