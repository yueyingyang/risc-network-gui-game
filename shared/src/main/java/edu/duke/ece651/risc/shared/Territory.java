package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;

/**
 * An class represents a territory
 *
 * Annotation JsonIdentityInfo is added for bidirectional serialization
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Territory {
    private String name;
    private String ownerName;
    private Army myArmy;
    private Set<Territory> neighbours;
    private Map<String, Army> attackerBuffer;

    /**
     * Add for Jackson deserialization
     */
    public Territory() {
    }

    /**
     * Create a territory object
     *
     * @param name is the name of the territory
     */
    public Territory(String name) {
        this.name = name;
        this.ownerName = null;
        this.myArmy = null;
        this.neighbours = new HashSet<>();
        this.attackerBuffer = new HashMap<>();
    }

    /**
     * Get the name of the territory
     *
     * @return the name of the territory
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the owner
     *
     * @return the name of the owner
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get neighbors
     *
     * @return the neighbor territories
     */
    public Set<Territory> getNeighbours() {
        return neighbours;
    }

    /**
     * Set the owner of the territory
     *
     * @param ownerName is the name of the owner
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Place the army on the territory
     *
     * @param myArmy is the army been placed on the territory
     */
    public void setMyArmy(Army myArmy) {
        this.myArmy = myArmy;
    }

    /**
     * Check if the given object is equal to this
     *
     * @param myObject is the given object
     * @return true if the object is equal to this, i.e. is an territory
     * and have the same name, else false
     */
    @Override
    public boolean equals(Object myObject) {
        if (myObject.getClass().equals(getClass())) {
            Territory terr = (Territory) myObject;
            return terr.getName().equals(getName());
        }
        return false;
    }

    /**
     * get the hash code of the territory
     *
     * @return the hash code of the territory
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Connect the territory to its neighbour
     *
     * @param neigh is an territory adjacent to the territory
     */
    public void addNeighbour(Territory neigh) {
        neighbours.add(neigh);
    }

    /**
     * Check the if the given territory is adjacent to this
     *
     * @param terr is the given territory
     * @return ture if they are adjacent else false
     */
    public boolean isAdjacent(Territory terr) {
        return neighbours.contains(terr);
    }

    /**
     * Add the given number of soldiers to myArmy
     *
     * @param numSoldiers is the number of soldiers to add
     */
    public void addSoldiersToArmy(int numSoldiers) {
        myArmy.addSoldiers(numSoldiers, "0");
    }

    /**
     * Remove the given number of soldiers to myArmy
     *
     * @param numSoldiers is the number of soldiers to add
     */
    public void removeSoldiersFromArmy(int numSoldiers) {
        myArmy.removeSoldiers(numSoldiers, "0");
    }

    /**
     * Get the number of soldier in myArmy
     *
     * @return the number of soldier in myArmy
     * -1 if myArmy has not been setup
     */
    public int getNumSoldiersInArmy() {
        if (myArmy == null) {
            return -1;
        }
        return myArmy.getNumSoldiers();
    }

    /**
     * Get the number of soldiers of the given type in myArmy
     *
     * @param type is the type of soldier
     * @return the number of soldiers with the given type
     * -1 if myArmy has not been setup
     */
    public int getNumSoldiersInArmy(String type) {
        if (myArmy == null) {
            return -1;
        }
        return myArmy.getNumSoldiers(type);

    }

    /**
     * Add attackers to attacker buffer
     *
     * @param attacker is the army that attack the territory
     */
    public void bufferAttacker(Army attacker) {
        String owner = attacker.getOwnerName();
        if (attackerBuffer.containsKey(owner)) {
            Army curr = attackerBuffer.get(owner);
            curr.mergeForce(attacker);
        } else {
            attackerBuffer.put(owner, attacker);
        }
    }

    /**
     * Resolve combat on the territory
     *
     * @param myRandom is the random object set by the game
     * @return the information of the combat resolve process
     */
    public String resolveCombat(Random myRandom) {
        if (attackerBuffer.size() == 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        List<Army> attackers = new ArrayList<>(attackerBuffer.values());
        Collections.shuffle(attackers, myRandom);
        temp.append("On territory ").append(this.getName()).append(":\n");
        Army defender = myArmy;
        for (Army attacker : attackers) {
            displayCombatInfo(defender, attacker, temp);
            defender = defender.fight(attacker, myRandom);
            temp.append(defender.getOwnerName()).append(" player wins.\n");
        }
        myArmy = defender;
        ownerName = myArmy.getOwnerName();
        attackerBuffer = new HashMap<>();
        return temp.toString();
    }

    /**
     * Display the information of one combat
     *
     * @param defender the defender in the combat
     * @param attacker the attacker in the combat
     * @param temp     is the string builder
     */
    protected void displayCombatInfo(Army defender, Army attacker, StringBuilder temp) {
        temp.append(defender.getOwnerName()).append(" player")
                .append("(").append(defender.getNumSoldiers()).append(" soldiers) ")
                .append("defends ").append(attacker.getOwnerName()).append(" player")
                .append("(").append(attacker.getNumSoldiers()).append(" soldiers). ");
    }

    /**
     * Get the number of soldiers in the attacker
     *
     * @param name is the name of the attacker owner
     * @return the number of soldiers in the attacker
     * or -1 if the attacker does not exist
     */
    public int getNumSoldiersInAttacker(String name) {
        Army attacker = attackerBuffer.get(name);
        if (attacker == null) {
            return -1;
        }
        return attacker.getNumSoldiers();
    }

    /**
     * Check if the two territories belong to the same owner
     *
     * @param terr is a territory
     * @return true if the two territories belong to the same owner else false
     */
    public boolean belongToSameOwner(Territory terr) {
        return ownerName.equals(terr.getOwnerName());
    }

    /**
     * Upgrade my army
     *
     * @param fromType    is the current type of the soldier
     * @param toType      is the type of the soldier after upgrading
     * @param numSoldiers is the number of soldiers
     * @param myInfo      is the player info
     */
    public void upgradeArmy(String fromType, String toType, int numSoldiers, PlayerInfo myInfo) {
        myArmy.upgradeForce(fromType, toType, numSoldiers, myInfo);
    }

}
