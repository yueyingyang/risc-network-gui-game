package edu.duke.ece651.risc.shared;

/**
 * A class to get info from constant
 */
public class GameUtil {
    /**
     * Get the bonus of the soldier at the indicated level
     *
     * @param level is the indicated level
     * @return the bonus
     */
    public static int getBonusAtLevel(int level) {
        return Constant.bonus.get(level);
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
     * @param fromLevel   is the current level of the soldiers
     * @param toLevel     is the level of the soldiers after upgrade
     * @param numSoldiers is the number of soldiers
     * @return the cost to upgrade soldiers
     */
    public static int getSoldierCost(int fromLevel, int toLevel, int numSoldiers) {
        return numSoldiers * (Constant.soldierCost.get(toLevel)
                - Constant.soldierCost.get(fromLevel));
    }
}
