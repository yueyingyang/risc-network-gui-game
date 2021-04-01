package edu.duke.ece651.risc.shared;

/**
 * A class to get info from constant
 */
public class GameUtil {
    /**
     * Get the bonus of the soldier at the indicated type
     *
     * @param type is the indicated type
     * @return the bonus
     */
    public static int getBonusAtLevel(String type) {
        return Constant.bonus.get(type);
    }

    /**
     * Get the cost to upgrade tech level by one from current level
     *
     * @param currLevel is the current technology level
     * @return the cost to upgrade the technology
     */
    public static int getTechCost(int currLevel) {
        return Constant.techCost.get(currLevel);
    }

    /**
     * Get the cost to upgrade soldiers
     *
     * @param fromType   is the current type of the soldiers
     * @param toType     is the type of the soldiers after upgrade
     * @param numSoldiers is the number of soldiers
     * @return the cost to upgrade soldiers
     */
    public static int getSoldierCost(String fromType, String toType, int numSoldiers) {
        return numSoldiers * (Constant.soldierCost.get(toType)
                - Constant.soldierCost.get(fromType));
    }
}
