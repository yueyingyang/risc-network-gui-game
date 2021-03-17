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
<<<<<<< HEAD
     * @param myMap     is the map of the game
=======
     * @param myMap is the map of the game
>>>>>>> bdef9c7b26d2cae9a325b42a9bf3d523c21e8e87
     * @param myChecker is the rule checker for the action
     */
    public void apply(GameMap myMap, Checker myChecker);

    /**
     * get the starting territory name
     * 
     * @return fromName is the name of starting territory
     */
    public String getFromName();

    /**
     * get the destionation territory name
     * 
     * @return toName is the name of destionation territory
     */
    public String getToName();

    /**
<<<<<<< HEAD
     * Get the number of soldiers
     *
     * @return the number of soldiers or -1 if the field does not exist
     */
    public int getNumSoldiers();

    /** 
=======
>>>>>>> bdef9c7b26d2cae9a325b42a9bf3d523c21e8e87
     * get number of soliders
     * 
     * @return numSoldiers is the number of soliders
     */
    public int getNumSoliders();
<<<<<<< HEAD

=======
>>>>>>> bdef9c7b26d2cae9a325b42a9bf3d523c21e8e87
}
