package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.game.GameUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * A class represent player info
 */
public class PlayerInfo {
    private String name;
    private int techLevel;
    private int foodResource;
    private int techResource;
    private boolean requested;
    private boolean cloakingTech;
    private Map<String, Territory> hasSeen;


    public PlayerInfo() {
    }

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
        this.requested = false;
        this.cloakingTech = false;
        this.hasSeen = new HashMap<>();
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
        requested = true;
    }

    /**
     * Make player's technology update in effect
     */
    public void effectTech() {
        if (requested) {
            techLevel += 1;
            requested = false;
        }
    }

    /**
     * Get whether the player has requested technology update in the turn
     *
     * @return true if the player has requested technology update in the turn else false
     */
    public boolean isRequested() {
        return requested;
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

    /**
     * Research the cloakingTech
     */
    public void researchCloakingTech() {
        cloakingTech = true;
        techResource -= Constant.RESEARCH_CLOAKING_COST;
    }

    /**
     * Check whether a player has the cloaking tech
     *
     * @return true if the player has else false
     */
    public boolean hasCloakingTech() {
        return cloakingTech;
    }

    /**
     * Save the information of a territory
     *
     * @param terr0 is a territory
     */
    public void seeTerr(Territory terr0) {
        Territory terr1 = new Territory(terr0);
        hasSeen.put(terr1.getName(), terr1);
    }

    /**
     * Get the previously seen territory
     *
     * @param name is the name of the territory
     * @return the previous seen territory, return null if the player haven't
     * seen the territory before
     */
    public Territory getPrevSeenTerr(String name) {
        return hasSeen.get(name);
    }
}
