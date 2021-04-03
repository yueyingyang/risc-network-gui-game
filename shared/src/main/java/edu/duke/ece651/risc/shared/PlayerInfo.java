package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.game.GameUtil;

/**
 * A class represent player info
 */
public class PlayerInfo {
    private String name;
    private int techLevel;
    private int foodResource;
    private int techResource;
    private boolean upgraded;


    /**
     * Construct a PlayerInfo object
     *
     * @param name         is the name of the player
     * @param techLevel    is the technology level
     * @param foodResource is the number of food
     * @param techResource is the number of technology resource
     */
    public PlayerInfo(String name, int techLevel, int foodResource, int techResource) {
        this.name = name;
        this.techLevel = techLevel;
        this.foodResource = foodResource;
        this.techResource = techResource;
        this.upgraded = false;
    }

    /**
     * Construct a PlayerInfo object
     *
     * @param name is the name of the player
     */
    public PlayerInfo(String name) {
        this(name, 1, 0, 0);
    }

    /**
     * Take player's update technology request
     */
    public void takeTech() {
        int cost = GameUtil.getTechCost(techLevel);
        consumeTech(cost);
        upgraded = true;
    }

    /**
     * Make player's technology update in effect
     */
    public void effectTech() {
        if (upgraded) {
            techLevel += 1;
            upgraded = false;
        }
    }

    /**
     * Get whether the player can upgrade the technology level
     *
     * @return true if the player can upgrade the technology level else false
     */
    public boolean canUpgrade() {
        return !upgraded;
    }

    /**
     * Consume technology resource
     *
     * @param cost is the technology cost
     */
    public void consumeTech(int cost) {
        techResource -= cost;
    }

    /**
     * Consume the food resource
     *
     * @param cost is the food cost
     */
    public void consumeFood(int cost) {
        foodResource -= cost;
    }

    /**
     * Get the technology level of the player
     *
     * @return the technology level
     */
    public int getTechLevel() {
        return techLevel;
    }

    /**
     * Get the number of food resource
     *
     * @return the number of food resource
     */
    public int getFoodResource() {
        return foodResource;
    }

    /**
     * Get the number of technology resource
     *
     * @return the number of technology resource
     */
    public int getTechResource() {
        return techResource;
    }

    /**
     * Get the name of the player
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Add resource
     *
     * @param myTerrs is the territories of the player
     */
    public void addResource(Iterable<Territory> myTerrs) {
        for (Territory t : myTerrs) {
            foodResource += t.getFoodProd();
            techResource += t.getTechProd();
        }
    }

}
