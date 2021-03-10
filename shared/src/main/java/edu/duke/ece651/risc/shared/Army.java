package edu.duke.ece651.risc.shared;

/**
 * An interface represents an army
 */
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
     * Fight with the attacker
     *
     * @param attacker is the army that attack the territory
     * @return the army that wins the fight
     */
    public Army fight(Army attacker);
}
