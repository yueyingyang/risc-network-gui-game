package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class represents a basic army
 */
public class BasicArmy implements Army {
    protected String ownerName;
    protected List<Soldier> force;

    /**
     * Construct a BasicArmy object
     *
     * @param ownerName   is the name of the owner of the army
     * @param numSoldiers is the number of soldiers in the force
     */
    public BasicArmy(String ownerName, int numSoldiers) {
        this.ownerName = ownerName;
        this.force = new ArrayList<>(Collections.nCopies(numSoldiers, new BasicSoldier()));
    }

    /**
     * get the name of the owner
     *
     * @return the name of the owner
     */
    @Override
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get the number of soldiers in the force
     *
     * @return the number of soldiers in the force
     */
    public int getNumSoldiers() {
        return force.size();
    }

    /**
     * Fight with the attacker
     *
     * @param attacker is the army that attack the territory
     * @return the army that wins the fight
     */
    @Override
    public Army fight(Army attacker) {
        // TODO
        return null;
    }

}
