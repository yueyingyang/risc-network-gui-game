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
    public void upgrade(String toType) {
        type = toType;
    }

    /**
     * Check whether a soldier has the indicated type
     *
     * @param myType is the indicated type
     * @return true if the soldier is has the indicated type else false
     */
    public boolean hasType(String myType) {
        return type.equals(myType);
    }

}
