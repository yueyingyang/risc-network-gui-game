package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A class represents a basic army
 */
public class BasicArmy implements Army {
    protected String ownerName;
    protected List<Soldier> force;

    /**
     * Construct a BasicArmy object
     *
     * @param ownerName is the name of the owner of the army
     * @param force     is the force in the army
     */
    public BasicArmy(String ownerName, List<Soldier> force) {
        this.ownerName = ownerName;
        this.force = force;
    }

    /**
     * Construct a BasicArmy object
     *
     * @param ownerName   is the name of the owner of the army
     * @param numSoldiers is the number of soldiers in the force
     */
    @ConstructorProperties({"ownerName", "numSoldiers"})
    public BasicArmy(String ownerName, int numSoldiers) {
        this(ownerName, new ArrayList<>(Collections.nCopies(numSoldiers, new BasicSoldier())));
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
     * Get the force of the army
     *
     * @return the force of the army
     */
    @Override
    public List<Soldier> getForce() {
        return force;
    }

    /**
     * Get the number of soldiers of the given type in the force
     *
     * @param type is the type of the soldier
     * @return the number of soldiers with the given type in the force
     */
    @Override
    public int getNumSoldiers(String type) {
        return Collections.frequency(force, new BasicSoldier(type));
    }

    /**
     * Get the number of soldiers in the force
     *
     * @return the number of soldiers in the force
     */
    @Override
    public int getNumSoldiers() {
        return force.size();
    }

    /**
     * Add soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers to add
     * @param type        is the type of the soldier
     */
    @Override
    public void addSoldiers(int numSoldiers, String type) {
        force.addAll(Collections.nCopies(numSoldiers, new BasicSoldier(type)));
    }

    /**
     * Remove the given number of soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers
     * @param type        is the type of the soldier
     */
    @Override
    public void removeSoldiers(int numSoldiers, String type) {
        Soldier s = new BasicSoldier(type);
        for (int i = 0; i < numSoldiers; i++) {
            force.remove(s);
        }
    }

    /**
     * Fight with the attacker
     *
     * @param attacker is the army that attack the territory
     * @param myRandom is the random object set by the game
     * @return the army that wins the fight
     */
    @Override
    public Army fight(Army attacker, Random myRandom) {
        while (getNumSoldiers() > 0 && attacker.getNumSoldiers() > 0) {
            fightOneRound(attacker, myRandom);
        }
        if (getNumSoldiers() > 0) {
            return this;
        }
        return attacker;
    }

    /**
     * Fight with the attacker for one round
     *
     * @param attacker is the army that attack the territory
     */
    protected void fightOneRound(Army attacker, Random myRandom) {
        List<Soldier> enemyForce = attacker.getForce();
        Soldier mySoldier = force.get(force.size() - 1);
        Soldier enemySoldier = enemyForce.get(enemyForce.size() - 1);
        int res = mySoldier.fight(enemySoldier, myRandom);
        if (res >= 0) {
            attacker.removeSoldiers(1, "0");
        } else {
            removeSoldiers(1, "0");
        }
    }

    /**
     * Merge the force of the same owner
     *
     * @param myArmy is the army from the same owner
     */
    @Override
    public void mergeForce(Army myArmy) {
        if (myArmy.getOwnerName().equals(getOwnerName())) {
            getForce().addAll(myArmy.getForce());
        }
    }

    /**
     * Upgrade the force
     *
     * @param fromType    is the current type of the soldier
     * @param toType      is the type of the soldier after upgrade
     * @param numSoldiers is the number of soldiers
     */
    @Override
    public void upgradeForce(String fromType, String toType, int numSoldiers) {
        removeSoldiers(numSoldiers, fromType);
        addSoldiers(numSoldiers, toType);
    }


}
