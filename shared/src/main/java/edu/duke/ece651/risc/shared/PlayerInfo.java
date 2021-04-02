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
     * Construct a playerInfo object
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
     * Construct a playerInfo object
     */
    public PlayerInfo() {
        this(1, 0, 0);
    }

    public void upgradeTech() {
        int cost = GameUtil.getTechCost(techLevel);
        consumeTech(cost);
        techLevel += 1;
    }

    public void consumeTech(int cost) {
        techResource -= cost;
    }

    public void consumeFood(int cost) {
        foodResource -= cost;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public int getFoodResource() {
        return foodResource;
    }

    public int getTechResource() {
        return techResource;
    }
}
