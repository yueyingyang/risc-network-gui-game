package edu.duke.ece651.risc.shared;

import java.beans.ConstructorProperties;
import java.util.*;

/**
 * A class represents a army
 */
public class Army {
    protected String ownerName;
    protected List<Soldier> force;

    /**
     * Construct a Army object
     *
     * @param ownerName is the name of the owner of the army
     * @param force     is the force in the army
     */
    public Army(String ownerName, List<Soldier> force) {
        this.ownerName = ownerName;
        this.force = force;
    }

    /**
     * Copy constructor of Army
     *
     * @param myArmy is a Army
     */
    public Army(Army myArmy) {
        this.ownerName = myArmy.ownerName;
        this.force = new ArrayList<>();
        for (Soldier s : myArmy.force) {
            this.force.add(new Soldier(s));
        }
    }

    /**
     * Construct a Army object
     *
     * @param ownerName   is the name of the owner of the army
     * @param numSoldiers is the number of soldiers in the force
     */
    @ConstructorProperties({"ownerName", "numSoldiers"})
    public Army(String ownerName, int numSoldiers) {
        this(ownerName, numSoldiers, "0");
    }

    /**
     * Construct a Army object
     *
     * @param ownerName   is the name of the owner of the army
     * @param numSoldiers is the number of soldiers in the force
     * @param type        is the type of the soldier
     */
    public Army(String ownerName, int numSoldiers, String type) {
        this(ownerName, new ArrayList<>(Collections.nCopies(numSoldiers, new Soldier(type))));
    }

    /**
     * get the name of the owner
     *
     * @return the name of the owner
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get the force of the army
     *
     * @return the force of the army
     */
    public List<Soldier> getForce() {
        return force;
    }

    /**
     * Get the number of soldiers of the given type in the force
     *
     * @param type is the type of the soldier
     * @return the number of soldiers with the given type in the force
     */
    public int getNumSoldiers(String type) {
        return Collections.frequency(force, new Soldier(type));
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
     * Add soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers to add
     * @param type        is the type of the soldier
     */
    public void addSoldiers(int numSoldiers, String type) {
        force.addAll(Collections.nCopies(numSoldiers, new Soldier(type)));
    }

    /**
     * Remove the given number of soldiers to the force
     *
     * @param numSoldiers is the number of the soldiers
     * @param type        is the type of the soldier
     */
    public void removeSoldiers(int numSoldiers, String type) {
        if (numSoldiers <= Collections.frequency(force, type)) {
            Soldier s = new Soldier(type);
            for (int i = 0; i < numSoldiers; i++) {
                force.remove(s);
            }
        }
    }

    /**
     * Fight with the attacker
     *
     * @param attacker is the army that attack the territory
     * @param myRandom is the random object set by the game
     * @return the army that wins the fight
     */
    public Army fight(Army attacker, Random myRandom) {
        Collections.sort(force);
        Collections.sort(attacker.getForce());
        int round = 1;
        while (getNumSoldiers() > 0 && attacker.getNumSoldiers() > 0) {
            fightOneRound(attacker, myRandom, round);
            round += 1;
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
    protected void fightOneRound(Army attacker, Random myRandom, int round) {
        List<Soldier> enemyForce = attacker.getForce();
        Soldier mySoldier, enemySoldier;
        if (round % 2 == 0) {
            mySoldier = force.get(0);
            enemySoldier = enemyForce.get(enemyForce.size() - 1);
        } else {
            mySoldier = force.get(force.size() - 1);
            enemySoldier = enemyForce.get(0);
        }
        int res = mySoldier.fight(enemySoldier, myRandom);
        if (res >= 0) {
            attacker.removeSoldiers(1, enemySoldier.getType());
        } else {
            removeSoldiers(1, mySoldier.getType());
        }
    }

    /**
     * Merge the force of the same owner
     *
     * @param myArmy is the army from the same owner
     */
    public void mergeForce(Army myArmy) {
        if (myArmy.getOwnerName().equals(getOwnerName())) {
            getForce().addAll(myArmy.getForce());
        }
    }

    /**
     * Get the string representation of the army
     *
     * @return the string representation of the army
     */
    @Override
    public String toString() {
        if (getNumSoldiers() == 0) {
            return "0 soldiers";
        }
        StringBuilder ans = new StringBuilder();
        Set<Soldier> keys = new HashSet<>(force);
        for (Soldier k : keys) {
            ans.append(Collections.frequency(force, k))
                    .append(" type-").append(k.getType()).append(" soldiers, ");
        }
        ans.delete(ans.length() - 2, ans.length());
        return ans.toString();
    }


}
