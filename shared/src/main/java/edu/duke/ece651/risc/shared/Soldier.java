package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Random;

/**
 * An interface represents a soldier
 */
@JsonDeserialize(as = BasicSoldier.class)
public interface Soldier {

    /**
     * Fight with the attacker
     *
     * @param attacker is the solder in the army that attacks the territory
     * @param myRandom is the random object set by the game
     * @return non-negative number if the defend soldier wins
     * else return negative number
     */
    public int fight(Soldier attacker, Random myRandom);

    /**
     * Get the type of the soldier
     *
     * @return the type
     */
    public String getType();
}
