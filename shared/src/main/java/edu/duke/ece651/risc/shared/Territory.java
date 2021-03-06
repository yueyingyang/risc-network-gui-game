package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Map.Entry;

import java.util.*;

/**
 * An class represents a territory
 * <p>
 * Annotation JsonIdentityInfo is added for bidirectional serialization
 */
@JsonIdentityInfo(
        scope = Territory.class,
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
    private Map<String, Army> spyCamp;
    private Map<String, Army> spyBuffer;
    private int cloakingBuffer;
    private String useShield;
    private Set<String> useSword;
    private Set<String> recvMissile;
    private Set<String> useShip;

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
        this(name, 5, 20, 20);
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
        this.spyCamp = new HashMap<>();
        this.spyBuffer = new HashMap<>();
        this.cloakingBuffer = 0;
        this.useShield = null;
        this.useSword = new TreeSet<>();
        this.recvMissile = new TreeSet<>();
        this.useShip = new TreeSet<>();
    }

    /**
     * Copy constructor of Territory
     *
     * @param terr is a territory
     */
    public Territory(Territory terr) {
        // immutable
        this.name = terr.name;
        this.ownerName = terr.ownerName;
        this.size = terr.size;
        this.foodProd = terr.foodProd;
        this.techProd = terr.techProd;
        this.cloaking = terr.cloaking;
        this.cloakingBuffer = terr.cloakingBuffer;

        // deep copy
        if (terr.myArmy == null) {
            this.myArmy = null;
        } else {
            this.myArmy = new Army(terr.myArmy);
        }

        this.spyCamp = new HashMap<>();
        for (Entry<String, Army> e : terr.spyCamp.entrySet()) {
            this.spyCamp.put(e.getKey(), new Army(e.getValue()));
        }

        // shallow copy
        this.neighbours = terr.neighbours;
        this.attackerBuffer = terr.attackerBuffer;
        this.spyBuffer = terr.spyBuffer;
        this.cloakingBuffer = terr.cloakingBuffer;
        this.useShield = terr.useShield;
        this.useSword = terr.useSword;
        this.recvMissile = terr.recvMissile;
        this.useShip = terr.useShip;
    }

    /**
     * Get the name of the territory
     *
     * @return the name of the territory
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeNeighbours(){
        this.neighbours = null;
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
        addSoldiersToArmy(numSoldiers, "0");
    }

    /**
     * Add the given number of soldiers with the indicated type to myArmy
     *
     * @param numSoldiers is the number of soldiers
     * @param type        is the type of the soldier
     */
    public void addSoldiersToArmy(int numSoldiers, String type) {
        if (myArmy == null) {
            myArmy = new Army(ownerName, numSoldiers, type);
        } else {
            myArmy.addSoldiers(numSoldiers, type);
        }
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
        if (myArmy != null) {
            myArmy.removeSoldiers(numSoldiers, type);
        }
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
     * Buffer army
     *
     * @param army   is an army
     * @param buffer is a buffer to store the enemy
     */
    protected void bufferArmy(Army army, Map<String, Army> buffer) {
        String owner = army.getOwnerName();
        if (buffer.containsKey(owner)) {
            Army curr = buffer.get(owner);
            curr.mergeForce(army);
        } else {
            buffer.put(owner, army);
        }
    }

    /**
     * Add attackers
     *
     * @param attacker is the army that attack the territory
     */
    public void bufferAttacker(Army attacker) {
        bufferArmy(attacker, attackerBuffer);
    }

    /**
     * Resolve combat on the territory
     *
     * @param myRandom is the random object set by the game
     * @return the information of the combat resolve process
     */
    public String resolveCombat(Random myRandom) {
        if (recvMissile.size() == 0 && useShield == null
                && useSword.size() == 0 && attackerBuffer.size() == 0
                && useShip.size() == 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        temp.append("On territory ").append(this.getName()).append(":\n");
        temp.append(resolveMissile());
        temp.append(displayShieldInfo());
        temp.append(displaySwordInfo());
        temp.append(displayShipInfo());
        List<Army> attackers = new ArrayList<>(attackerBuffer.values());
        Collections.shuffle(attackers, myRandom);
        Army defender = myArmy;
        for (Army attacker : attackers) {
            displayCombatInfo(defender, attacker, temp);
            defender = defender.fight(attacker, myRandom, getAttackerBonus(attacker.getOwnerName()), getDefenderBonus(defender.getOwnerName()));
            temp.append(defender.getOwnerName()).append(" player wins.\n");
        }
        myArmy = defender;
        ownerName = myArmy.getOwnerName();
        attackerBuffer = new HashMap<>();
        useShield = null;
        useSword = new TreeSet<>();
        recvMissile = new TreeSet<>();
        useShip = new TreeSet<>();
        return temp.toString();
    }

    /**
     * Resolve missiles for the territory
     *
     * @return the string that contains missile attacks information
     */
    public String resolveMissile() {
        StringBuilder temp = new StringBuilder();
        if (!recvMissile.isEmpty()) {
            temp.append("Received missile(s) from ");
            for (String playerName : recvMissile) {
                temp.append(playerName).append(", ");
            }
            temp.delete(temp.length() - 2, temp.length());
            temp.append(" player.\n");
            setMyArmy(new Army(getOwnerName(), new ArrayList<>()));
        }
        return temp.toString();
    }

    /**
     * Display shield info
     *
     * @return show whether the player uses shield
     */
    public String displayShieldInfo() {
        StringBuilder temp = new StringBuilder();
        if (useShield != null) {
            temp.append(ownerName).append(" player use shield.\n");
        }
        return temp.toString();
    }

    /**
     * Display sword info
     *
     * @return show the player that uses sword
     */
    public String displaySwordInfo() {
        StringBuilder temp = new StringBuilder();
        if (useSword.size() > 0) {
            for (String name : useSword) {
                temp.append(name).append(", ");
            }
            temp.delete(temp.length() - 2, temp.length());
            temp.append(" player use sword.\n");
        }
        return temp.toString();
    }

    /**
     * Display ship info
     *
     * @return show the player that uses ship
     */
    public String displayShipInfo() {
        StringBuilder temp = new StringBuilder();
        if (useShip.size() > 0) {
            for (String name : useShip) {
                temp.append(name).append(", ");
            }
            temp.delete(temp.length() - 2, temp.length());
            temp.append(" player use ship.\n");
        }
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
     * @param playerName is the player name
     * @return the number of soldiers in the attacker
     */
    public int getNumSoldiersInAttacker(String playerName) {
        if (!attackerBuffer.containsKey(playerName)) {
            return 0;
        }
        return attackerBuffer.get(playerName).getNumSoldiers();
    }

    /**
     * Get the number of soldier in the attacker with the given type
     *
     * @param playerName is the player name
     * @param type       is the type of the soldier
     * @return the number of soldiers in the attacker
     */
    public int getNumSoldiersInAttacker(String playerName, String type) {
        if (!attackerBuffer.containsKey(playerName)) {
            return 0;
        }
        return attackerBuffer.get(playerName).getNumSoldiers(type);
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
     * Add spies
     *
     * @param playerName is the player name
     * @param numSpies   is the number spies
     */
    public void addSpies(String playerName, int numSpies) {
        Army spies = new Army(playerName, numSpies, "0");
        bufferArmy(spies, spyCamp);
    }

    /**
     * Buffer spies
     *
     * @param playerName is the player name
     * @param numSpies   is the number spies
     */
    public void bufferSpies(String playerName, int numSpies) {
        Army spies = new Army(playerName, numSpies, "0");
        bufferArmy(spies, spyBuffer);
    }

    /**
     * Remove spies
     *
     * @param playerName is the player name
     * @param numSpies   is the number of spyCamp
     */
    public void removeSpies(String playerName, int numSpies) {
        if (spyCamp.containsKey(playerName)) {
            spyCamp.get(playerName).removeSoldiers(numSpies, "0");
        }
    }

    /**
     * Take effect of the spy move
     */
    public void effectSpyMove() {
        for (Army spies : spyBuffer.values()) {
            bufferArmy(spies, spyCamp);
        }
        spyBuffer = new HashMap<>();
    }

    /**
     * Get the number of spyCamp of the given player
     *
     * @param playerName is the playerName
     * @return the number of spyCamp of the enemy
     */
    public int getNumSpies(String playerName) {
        if (!spyCamp.containsKey(playerName)) {
            return 0;
        }
        return spyCamp.get(playerName).getNumSoldiers();
    }

    /**
     * Get the number of spies
     *
     * @param playerName is the player playerName
     * @return the number of my spyCamp
     */
    protected int getBufferedNumSpies(String playerName) {
        if (!spyBuffer.containsKey(playerName)) {
            return 0;
        }
        return spyBuffer.get(playerName).getNumSoldiers();
    }

    /**
     * Check if the territory is an immediately adjacent enemy to the player
     *
     * @param playerName is the player name
     * @return ture if the the territory is an immediately adjacent enemy to
     * the player else false
     */
    protected boolean isAdjacentEnemy(String playerName) {
        if (ownerName.equals(playerName)) {
            return false;
        }
        for (Territory t : neighbours) {
            if (t.ownerName.equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add cloaking
     */
    public void bufferCloaking() {
        cloakingBuffer += Constant.CLOAKING_TURNS;
    }

    /**
     * Take effect of the cloaking
     */
    public void effectCloaking() {
        cloaking += cloakingBuffer;
        cloakingBuffer = 0;
    }

    /**
     * Use cloaking
     */
    public void consumeCloaking() {
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
     * Get the temp cloaking
     *
     * @return the temp cloaking
     */
    protected int getTempCloaking() {
        return cloakingBuffer;
    }

    /**
     * Check if the territory if visible to certain player
     *
     * @param myInfo is the playerInfo
     * @return true if the territory is visible to the player else false
     */
    public boolean isVisible(PlayerInfo myInfo) {
        String myName = myInfo.getName();
        boolean ownTerr = ownerName.equals(myName);
        boolean adjEnemy = cloaking == 0 && isAdjacentEnemy(myName);
        boolean hasSpy = getNumSpies(myName) > 0;
        if (ownTerr || adjEnemy || hasSpy) {
            myInfo.seeTerr(this);
            return true;
        }
        return false;
    }

    /**
     * get the defender's bonus in this round
     *
     * @param playerName the corresponding player's name
     * @return shield bonus if the player used shield or used sword before win last round, otherwise return 0
     */
    public int getDefenderBonus(String playerName) {
        if (playerName.equals(useShield) || useSword.contains(playerName)) {
            return Constant.SHIELD_BONUS;
        }
        return 0;
    }

    /**
     * get the attacker's bonus in this round
     *
     * @param playerName the player's name
     * @return sword bonus if the player used sword
     */
    public int getAttackerBonus(String playerName) {
        if (!useSword.contains(playerName)) {
            return 0;
        }
        return Constant.SWORD_BONUS;
    }

    /**
     * add the player to the set of use sword
     *
     * @param playerName the name of the player uses sword
     */
    public void setUseSword(String playerName) {
        useSword.add(playerName);
    }

    /**
     * let the player to be the defender that use shield,
     * note that it should be the original owner of the territory or null
     *
     * @param playerName the name of the player uses shield
     */
    public void setUseShield(String playerName) {
        useShield = playerName;
    }

    /**
     * add player to the missile buffer
     *
     * @param playerName the name of player uses missile
     */
    public void applyMissile(String playerName) {
        recvMissile.add(playerName);
    }

    /**
     * check whether this player used missile in this round
     *
     * @param playerName the name of player
     * @return true if the player used missile otherwise return false
     */
    public boolean hasMissile(String playerName) {
        return recvMissile.contains(playerName);
    }

    /**
     * Record the player that use the ship
     *
     * @param playerName is the player name
     */
    public void addUseShip(String playerName) {
        useShip.add(playerName);
    }

    /**
     * Return if the player has use the ship
     *
     * @param playerName is the player name
     * @return if the player has use the ship
     */
    public boolean hasUseShip(String playerName) {
        return useShip.contains(playerName);
    }

}
