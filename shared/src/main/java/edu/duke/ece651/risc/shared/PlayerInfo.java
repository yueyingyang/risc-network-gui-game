package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.game.GameUtil;

/**
 * A class represent player info
 */
public class PlayerInfo {
    private int techLevel;
    private int foodResource;
    private int techResource;

    /**
     * Construct a PlayerInfo object
     *
     * @param techLevel    is the technology level
     * @param foodResource is the number of food
     * @param techResource is the number of technology resource
     */
    public PlayerInfo(int techLevel, int foodResource, int techResource) {
        this.techLevel = techLevel;
        this.foodResource = foodResource;
        this.techResource = techResource;
    }

    /**
     * Construct a PlayerInfo object
     */
    public PlayerInfo() {
        this(1, 0, 0);
    }

    /**
     * Upgrade technology by one level
     */
    public void upgradeTech() {
        int cost = GameUtil.getTechCost(techLevel);
        consumeTech(cost);
        techLevel += 1;
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
}
