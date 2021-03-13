package edu.duke.ece651.risc.shared;

/**
 * An interface represent an action entry
 */
public interface ActionEntry {

    /**
     * Apply the action on the action entry
     *
     * @param myMap is the map of the game
     */
    public void apply(GameMap myMap);
}
