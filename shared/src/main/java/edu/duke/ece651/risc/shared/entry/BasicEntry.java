package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.entry.ActionEntry;

/**
 * An class represents a basic entry
 */
public abstract class BasicEntry implements ActionEntry {
    protected String fromName;
    protected String toName;
    protected int numSoldiers;
    protected String playerName;

    /**
     * Construct a basic entry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     */
    public BasicEntry(String fromName, String toName, int numSoldiers, String playerName) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
        this.playerName = playerName;
    }

    /**
     * Get the name of the from-territory
     *
     * @return the name of the from territory or null if the field does not exist
     */
    @Override
    public String getFromName() {
        return fromName;
    }

    /**
     * Get the name of the to-territory
     *
     * @return the name of the to territory or null if the field does not exist
     */
    @Override
    public String getToName() {
        return toName;
    }

    /**
     * Get the number of soldiers
     *
     * @return the number of soldiers or -1 if the field does not exist
     */
    @Override
    public int getNumSoldiers() {
        return numSoldiers;
    }

    /**
     * Get the name of the player
     *
     * @return the name of the player
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }
}
