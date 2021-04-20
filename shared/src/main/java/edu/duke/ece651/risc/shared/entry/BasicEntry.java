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
    protected int numProds;
    protected String prodName;

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
        this.numProds = 0;
        this.prodName = null;
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

    /**
     * Get the name of from type
     *
     * @return the name of from type
     */
    @Override
    public String getFromType() {
        return fromType;
    }

    /**
     * Get the name of to type
     *
     * @return the name of to type
     */
    @Override
    public String getToType() {
        return toType;
    }

    /**
     * Get whether the attack is using ship
     *
     * @return true if use ship otherwise return false
     */
    @Override
    public boolean isUseShip(){ return useShip; }

    /**
     * Get the number of prods to buy
     *
     * @return the number of prods to buy
     */
    @Override
    public int getNumProds() {
        return numProds;
    }

    /**
     * Get the name of prod to buy
     *
     * @return the name of prod to buy
     */
    @Override
    public String getProdName() {
        return prodName;
    }
}
