package edu.duke.ece651.risc.shared;

import java.util.Random;

/**
 * An interface represents a soldier
 */
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
}
