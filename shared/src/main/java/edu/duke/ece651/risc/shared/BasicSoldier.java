package edu.duke.ece651.risc.shared;

import java.util.Random;

public class BasicSoldier implements Soldier {

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
}
