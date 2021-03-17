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
}
