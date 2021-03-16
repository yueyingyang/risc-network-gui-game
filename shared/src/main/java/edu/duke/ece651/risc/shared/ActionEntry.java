package edu.duke.ece651.risc.shared;

/**
 * An interface represent an action entry
 */
public interface ActionEntry {

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
}
