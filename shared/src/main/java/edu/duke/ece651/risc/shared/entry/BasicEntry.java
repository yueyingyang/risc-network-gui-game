package edu.duke.ece651.risc.shared.entry;


/**
 * An class represents a basic entry
 */
public abstract class BasicEntry implements ActionEntry {
    protected String fromName;
    protected String toName;
    protected int numSoldiers;
    protected String playerName;
    protected String fromType;
    protected String toType;
    protected boolean useShip;

    /**
     * Construct a basic entry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     * @param fromType    is the current type of the soldier
     * @param toType      is the type of the soldier after update
     */
    public BasicEntry(String fromName, String toName, int numSoldiers, String playerName,
                      String fromType, String toType) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
        this.playerName = playerName;
        this.fromType = fromType;
        this.toType = toType;
        this.useShip = false;
    }

    /*
    /**
     * Construct a basic entry
     *
     * @param fromName    is the name of the from-territory
     * @param toName      is the name of the to-territory
     * @param numSoldiers is the number of soldiers
     * @param playerName  is the name of the player
     * @param fromType    is the current type of the soldier
     * @param toType      is the type of the soldier after update

    public BasicEntry(String fromName, String toName, int numSoldiers, String playerName,
                      String fromType, String toType, boolean useShip) {
        this.fromName = fromName;
        this.toName = toName;
        this.numSoldiers = numSoldiers;
        this.playerName = playerName;
        this.fromType = fromType;
        this.toType = toType;
        this.useShip = useShip;
    }
    */

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

    public String getFromType() {
        return fromType;
    }

    public String getToType() {
        return toType;
    }

    public boolean isUseShip(){ return useShip; }
}
