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

<<<<<<< HEAD
  /**
   * Apply the action on the action entry
   *
   * @param myMap     is the map of the game
   * @param myChecker is the rule checker for the action
   */
  public void apply(GameMap myMap, Checker myChecker);
=======
    /**
     * Apply the action on the action entry
     *
     * @param myMap is the map of the game
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
     * get number of soliders
     * 
     * @return numSoldiers is the number of soliders
     */
    public int getNumSoliders();
>>>>>>> 124e13702c668145b5e1205eab27ca4f9d76880f
}
