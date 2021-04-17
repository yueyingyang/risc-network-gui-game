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
    private int size;
    private int foodProd;
    private int techProd;
    private int cloaking;
    private Army mySpies;
    private Map<String, Army> enemySpies;

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
        this(name, 0, 0, 0);
    }

    /**
     * Create a territory object
     *
     * @param name     is the name of the territory
     * @param size     is the size of the territory
     * @param foodProd is the food production of the territory
     * @param techProd is the tech production of the territory
     */
    public Territory(String name, int size, int foodProd, int techProd) {
        this.name = name;
        this.ownerName = null;
        this.myArmy = null;
        this.neighbours = new HashSet<>();
        this.attackerBuffer = new HashMap<>();
        this.size = size;
        this.foodProd = foodProd;
        this.techProd = techProd;
        this.cloaking = 0;
        this.mySpies = null;
        this.enemySpies = new HashMap<>();
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
     * Get the size of the territory
     *
     * @return the size of the territory
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the food production of the territory
     *
     * @return the food production of the territory
     */
    public int getFoodProd() {
        return foodProd;
    }

    /**
     * Get the technology production of the territory
     *
     * @return the technology production of the territory
     */
    public int getTechProd() {
        return techProd;
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
     * Add the given number of basic soldiers to myArmy
     *
     * @param numSoldiers is the number of soldiers
     */
    public void addSoldiersToArmy(int numSoldiers) {
        myArmy.addSoldiers(numSoldiers, "0");
    }

    /**
     * Add the given number of soldiers with the indicated type to myArmy
     *
     * @param numSoldiers is the number of soldiers
     * @param type        is the type of the soldier
     */
    public void addSoldiersToArmy(int numSoldiers, String type) {
        myArmy.addSoldiers(numSoldiers, type);
    }

    /**
     * Remove the given number of basic soldiers from myArmy
     *
     * @param numSoldiers is the number of soldiers
     */
    public void removeSoldiersFromArmy(int numSoldiers) {
        myArmy.removeSoldiers(numSoldiers, "0");
    }

    /**
     * Remove the given number of soldiers with the indicated type from myArmy
     *
     * @param numSoldiers is the number of soldiers
     * @param type        is the type of the soldier
     */
    public void removeSoldiersFromArmy(int numSoldiers, String type) {
        myArmy.removeSoldiers(numSoldiers, type);
    }

    /**
     * Get the number of soldier in myArmy
     *
     * @return the number of soldier in myArmy
     */
    public int getNumSoldiersInArmy() {
        if (myArmy == null) {
            return 0;
        }
        return myArmy.getNumSoldiers();
    }

    /**
     * Get the number of soldiers of the given type in myArmy
     *
     * @param type is the type of soldier
     * @return the number of soldiers with the given type
     */
    public int getNumSoldiersInArmy(String type) {
        if (myArmy == null) {
            return 0;
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
        temp.append("Defender: ").append(defender.getOwnerName()).append(" player(")
                .append(defender.toString()).append(")\n")
                .append("Attacker: ").append(attacker.getOwnerName()).append(" player(")
                .append(attacker.toString()).append(")\n");
    }

    /**
     * Get the number of soldiers in the attacker
     *
     * @param name is the name of the attacker owner
     * @return the number of soldiers in the attacker
     */
    public int getNumSoldiersInAttacker(String name) {
        if (!attackerBuffer.containsKey(name)) {
            return 0;
        }
        return attackerBuffer.get(name).getNumSoldiers();
    }

    /**
     * Get the number of soldier in the attacker with the given type
     *
     * @param name is the name of the attacker owner
     * @param type is the type of the soldier
     * @return the number of soldiers in the attacker
     */
    public int getNumSoldiersInAttacker(String name, String type) {
        if (!attackerBuffer.containsKey(name)) {
            return 0;
        }
        return attackerBuffer.get(name).getNumSoldiers(type);
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
     * Add cloaking
     */
    public void addCloaking() {
        cloaking += 3;
    }

    /**
     * Use cloaking
     */
    public void useCloaking() {
        if (cloaking > 0) {
            cloaking -= 1;
        }
    }

    /**
     * Get the cloaking
     *
     * @return the cloaking
     */
    public int getCloaking() {
        return cloaking;
    }

    /**
     * Add spies
     *
     * @param numSpies is the number of spies
     */
    public void addSpies(int numSpies) {
        if (mySpies == null) {
            mySpies = new BasicArmy(ownerName, 1);
        }
        mySpies.addSoldiers(numSpies, "0");
    }

    /**
     * Remove spies
     *
     * @param numSpies is the number of spies
     */
    public void removeSpies(int numSpies) {
        mySpies.removeSoldiers(numSpies, "0");
    }

    /**
     * Get the number of spies
     *
     * @return the number of spies
     */
    public int getNumSpies() {
        if (mySpies == null) {
            return 0;
        }
        return mySpies.getNumSoldiers();
    }

    /**
     * Get the number of spies of the given enemy
     *
     * @param name is the name of the enemy
     * @return the number of spies of the enemy
     */
    public int getNumEnemySpies(String name) {
        if (!enemySpies.containsKey(name)) {
            return 0;
        }
        return enemySpies.get(name).getNumSoldiers();
    }


}
