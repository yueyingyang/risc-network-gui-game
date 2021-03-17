package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface represent an action entry
 *
 * Annotation added for jackson subtype deserialization: https://www.baeldung.com/jackson-inheritance
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlaceEntry.class, name = "place"),
        @JsonSubTypes.Type(value = AttackEntry.class, name = "attack"),
        @JsonSubTypes.Type(value = MoveEntry.class, name = "move")
})
public interface ActionEntry {

    /**
     * Apply the action on the action entry
     *
     * @param myMap     is the map of the game
     * @param myChecker is the rule checker for the action
     */
    public void apply(GameMap myMap, Checker myChecker);

    /**
<<<<<<< HEAD
     * Get the name of the from-territory
     *
     * @return the name of the from-territory or null if the field does not exist
=======
     * get the starting territory name
     * 
     * @return fromName is the name of starting territory
>>>>>>> checker
     */
    public String getFromName();

    /**
<<<<<<< HEAD
     * Get the name of the to-territory
     *
     * @return the name of the to-territory or null if the field does not exist
=======
     * get the destionation territory name
     * 
     * @return toName is the name of destionation territory
>>>>>>> checker
     */
    public String getToName();

    /**
<<<<<<< HEAD
     * Get the number of soldiers
     *
     * @return the number of soldiers or -1 if the field does not exist
     */
    public int getNumSoldiers();

=======
     * get number of soliders
     * 
     * @return numSoldiers is the number of soliders
     */
    public int getNumSoliders();
>>>>>>> checker
}
