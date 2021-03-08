package edu.duke.ece651.risc.shared;

/**
 * An interface represents an army
 */
public interface Army {

    /**
     * Fight with the attacker
     *
     * @param attacker is the army that '/attack the territory
     * @return the army that wins the fight
     */
    Army fight(Army attacker);
}
