package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Random;

/**
 * An interface represents an army
 */
@JsonDeserialize(as = BasicArmy.class)
public interface Army {

    /**
     * get the name of the owner
     *
     * @return the name of the owner
     */
    public String getOwnerName();

    /**
     * Get the number of soldiers in the force
     *
     * @return the number of soldiers in the force
     */
    public int getNumSoldiers();

    /**
     * Add the given number of soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers to add
     */
    public void addSoldiers(int numSoldiers);

    /**
     * Remove the given number of soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers
     * @param type is the type of the soldier
     */
    public void removeSoldiers(int numSoldiers, String type);

    /**
     * Fight with the attacker
     *
     * @param attacker is the army that attack the
     * @param myRandom is the random object set by the game
     * @return the army that wins the fight
     */
    public Army fight(Army attacker, Random myRandom);

    /**
     * Get the force of the army
     *
     * @return the force of the army
     */
    public List<Soldier> getForce();

    /**
     * Merge the force of the same owner
     *
     * @param myArmy is the army from the same owner
     */
    public void mergeForce(Army myArmy);
}
